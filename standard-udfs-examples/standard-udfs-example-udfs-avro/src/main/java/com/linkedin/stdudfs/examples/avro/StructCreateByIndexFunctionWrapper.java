/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.stdudfs.examples.avro;

import com.google.common.collect.ImmutableList;
import com.linkedin.stdudfs.api.udf.StdUDF;
import com.linkedin.stdudfs.api.udf.TopLevelStdUDF;
import com.linkedin.stdudfs.avro.StdUdfWrapper;
import com.linkedin.stdudfs.examples.StructCreateByIndexFunction;
import java.util.List;


public class StructCreateByIndexFunctionWrapper extends StdUdfWrapper {
  @Override
  protected List<? extends StdUDF> getStdUdfImplementations() {
    return ImmutableList.of(new StructCreateByIndexFunction());
  }

  @Override
  protected Class<? extends TopLevelStdUDF> getTopLevelUdfClass() {
    return StructCreateByIndexFunction.class;
  }
}
