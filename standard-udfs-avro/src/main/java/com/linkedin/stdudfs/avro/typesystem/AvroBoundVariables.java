/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.stdudfs.avro.typesystem;

import com.linkedin.stdudfs.typesystem.AbstractBoundVariables;
import com.linkedin.stdudfs.typesystem.AbstractTypeSystem;
import org.apache.avro.Schema;


public class AvroBoundVariables extends AbstractBoundVariables<Schema> {
  @Override
  protected AbstractTypeSystem<Schema> getTypeSystem() {
    return new AvroTypeSystem();
  }
}
