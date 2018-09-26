/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.stdudfs.api.data;

/** A Standard UDF data type for representing booleans. */
public interface StdBoolean extends StdData {

  /** Returns the underlying boolean value. */
  boolean get();
}
