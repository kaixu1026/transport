/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.transport.trino;

import com.google.common.collect.ImmutableSet;
import com.linkedin.transport.api.StdFactory;

import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.api.data.ArrayData;
import com.linkedin.transport.api.data.MapData;
import com.linkedin.transport.api.data.RowData;
import com.linkedin.transport.trino.data.TrinoArrayData;
import com.linkedin.transport.trino.data.TrinoMapData;
import com.linkedin.transport.trino.data.TrinoRowData;
import io.trino.metadata.FunctionBinding;
import io.trino.metadata.FunctionDependencies;
import io.trino.metadata.Metadata;
import io.trino.metadata.OperatorNotFoundException;
import io.trino.spi.function.InvocationConvention;
import io.trino.spi.function.OperatorType;
import io.trino.spi.type.ArrayType;
import io.trino.spi.type.MapType;
import io.trino.spi.type.RowType;
import io.trino.spi.type.Type;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.stream.Collectors;

import static io.trino.metadata.SignatureBinder.*;
import static io.trino.sql.analyzer.TypeSignatureTranslator.*;


public class TrinoFactory implements StdFactory {

  final FunctionBinding functionBinding;
  final FunctionDependencies functionDependencies;
  final Metadata metadata;

  public TrinoFactory(FunctionBinding functionBinding, FunctionDependencies functionDependencies) {
    this.functionBinding = functionBinding;
    this.functionDependencies = functionDependencies;
    this.metadata = null;
  }

  public TrinoFactory(FunctionBinding functionBinding, Metadata metadata) {
    this.functionBinding = functionBinding;
    this.functionDependencies = null;
    this.metadata = metadata;
  }

  @Override
  public ArrayData createArray(StdType stdType, int expectedSize) {
    return new TrinoArrayData((ArrayType) stdType.underlyingType(), expectedSize, this);
  }

  @Override
  public ArrayData createArray(StdType stdType) {
    return createArray(stdType, 0);
  }

  @Override
  public MapData createMap(StdType stdType) {
    return new TrinoMapData((MapType) stdType.underlyingType(), this);
  }

  @Override
  public TrinoRowData createStruct(List<String> fieldNames, List<StdType> fieldTypes) {
    return new TrinoRowData(fieldNames,
        fieldTypes.stream().map(stdType -> (Type) stdType.underlyingType()).collect(Collectors.toList()), this);
  }

  @Override
  public TrinoRowData createStruct(List<StdType> fieldTypes) {
    return new TrinoRowData(
        fieldTypes.stream().map(stdType -> (Type) stdType.underlyingType()).collect(Collectors.toList()), this);
  }

  @Override
  public RowData createStruct(StdType stdType) {
    return new TrinoRowData((RowType) stdType.underlyingType(), this);
  }

  @Override
  public StdType createStdType(String typeSignature) {
    if (metadata != null) {
      return TrinoWrapper.createStdType(
          metadata.getType(applyBoundVariables(parseTypeSignature(typeSignature, ImmutableSet.of()), functionBinding)));
    }
    return TrinoWrapper.createStdType(
          functionDependencies.getType(applyBoundVariables(parseTypeSignature(typeSignature, ImmutableSet.of()), functionBinding)));
  }

  public MethodHandle getOperatorHandle(
      OperatorType operatorType,
      List<Type> argumentTypes,
      InvocationConvention invocationConvention) throws OperatorNotFoundException {
    if (metadata != null) {
      return metadata.getScalarFunctionInvoker(metadata.resolveOperator(operatorType, argumentTypes),
          invocationConvention).getMethodHandle();
    }
    return functionDependencies.getOperatorInvoker(operatorType, argumentTypes, invocationConvention).getMethodHandle();
  }
}
