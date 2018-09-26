/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.stdudfs.avro.types;

import com.linkedin.stdudfs.api.types.StdArrayType;
import com.linkedin.stdudfs.api.types.StdType;
import com.linkedin.stdudfs.avro.AvroWrapper;
import org.apache.avro.Schema;


public class AvroArrayType implements StdArrayType {
  private final Schema _schema;

  public AvroArrayType(Schema schema) {
    _schema = schema;
  }

  @Override
  public StdType elementType() {
    return AvroWrapper.createStdType(_schema.getElementType());
  }

  @Override
  public Object underlyingType() {
    return _schema;
  }
}
