/*
 * Copyright (c) 1998-2015 Caucho Technology -- all rights reserved
 *
 * This file is part of Baratine(TM)
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Baratine is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Baratine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Baratine; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Alex Rojkov
 */

package com.caucho.v5.autoconf.bean;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.caucho.v5.beans.BeanValidator;
import com.caucho.v5.beans.BeanValidatorNull;
import com.caucho.v5.config.IncludeOnClass;
import com.caucho.v5.config.Priority;

import io.baratine.config.Include;
import io.baratine.inject.Bean;

@Include
@IncludeOnClass(Validation.class)
public class ValidatorProviderJsr303
{
  private static final Logger log
    = Logger.getLogger(ValidatorProviderJsr303.class.getName());

  private final ValidatorFactory _validatorFactory;

  public ValidatorProviderJsr303()
  {
    ValidatorFactory validatorFactory = null;

    try {
      validatorFactory = Validation.buildDefaultValidatorFactory();

      if (validatorFactory.getValidator() == null) {
        validatorFactory = null;
      }
    } catch (Throwable t) {
      log.log(Level.WARNING, t.toString(), t);
    }

    _validatorFactory = validatorFactory;
  }

  @Bean
  @Priority(-10)
  public BeanValidator getValidator()
  {
    if (_validatorFactory != null) {
      return new BeanValidatorJsr303(_validatorFactory.getValidator());
    }
    else {
      return BeanValidatorNull.instance;
    }
  }

  @Override
  public String toString()
  {
    return this.getClass().getSimpleName() + "["
           + _validatorFactory
           + ']';
  }
}
