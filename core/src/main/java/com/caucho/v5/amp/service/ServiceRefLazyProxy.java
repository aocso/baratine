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
 * @author Scott Ferguson
 */

package com.caucho.v5.amp.service;

import java.lang.reflect.Type;

import com.caucho.v5.amp.ServiceManagerAmp;
import com.caucho.v5.amp.ServiceRefAmp;
import com.caucho.v5.amp.actor.ActorAmpLazyProxy;
import com.caucho.v5.amp.spi.ActorAmp;
import com.caucho.v5.amp.spi.MethodRefAmp;

/**
 * Lazy init proxy
 */
public class ServiceRefLazyProxy extends ServiceRefLazy
{
  private final String _address;
    
  public ServiceRefLazyProxy(ServiceManagerAmp manager, String address)
  {
    super(manager);

    _address = address;
  }
  
  @Override
  public String address()
  {
    return _address;
  }
  
  /*
  @Override
  public ServiceManagerAmp getManager()
  {
    // baratine/a240 vs baratine/2102
    
    return Amp.getContextManager();
  }
  */
  
  @Override
  protected ServiceRefAmp newDelegate()
  {
    ServiceRefAmp delegate = manager().service(_address);

    if (delegate != null && ! delegate.isClosed()) {
      return delegate;
    }
    else {
      return new ServiceRefNull(manager(), _address);
    }
  }
  
  @Override
  public ActorAmp getActor()
  {
    ServiceRefAmp delegate = delegate();
    
    if (! delegate.isClosed()) {
      return delegate.getActor();
    }
    else {
      return new ActorAmpLazyProxy(this);
    }
  }

  @Override
  public MethodRefAmp getMethod(String methodName)
  {
    MethodRefAmp methodRef = delegate().getMethod(methodName);

    if (! methodRef.isClosed()) {
      return methodRef;
    }
    else {
      return new MethodRefLazyProxy(this, methodName);
    }
  }
  
  
  @Override
  public MethodRefAmp getMethod(String methodName, Type returnType)
  {
    MethodRefAmp methodRef = delegate().getMethod(methodName, returnType);
    
    if (! methodRef.isClosed()) {
      return methodRef;
    }
    else {
      // XXX: needs type
      return new MethodRefLazyProxy(this, methodName);
    }
  }
  
  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _address + "]";
  }
}