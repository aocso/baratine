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

package com.caucho.v5.amp.marshal;

import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

import com.caucho.v5.loader.EnvironmentLocal;

/**
 * importing classes from a pod to another classloader.
 * 
 * Classes must be converted for different ports.
 */
public class PodImportContext
{
  private static final EnvironmentLocal<PodImportContext> _localContext
    = new EnvironmentLocal<>();
  
  private final ClassLoader _targetLoader;
  
  private final WeakHashMap<ClassLoader,SoftReference<PodImport>> _exportMap
    = new WeakHashMap<>();

  private PodImportContext(ClassLoader targetLoader)
  {
    _targetLoader = targetLoader;
  }
  
  public static PodImportContext create()
  {
    return create(Thread.currentThread().getContextClassLoader());
  }
  
  public static PodImportContext create(ClassLoader targetLoader)
  {
    synchronized (_localContext) {
      PodImportContext context = _localContext.get(targetLoader);
      
      if (context == null) {
        context = new PodImportContext(targetLoader);
        
        _localContext.set(context, targetLoader);
      }
      
      return context;
    }
  }
  
  public PodImport getPodImport(ClassLoader sourceLoader)
  {
    synchronized (_exportMap) {
      SoftReference<PodImport> exportRef = _exportMap.get(sourceLoader);
      
      if (exportRef != null) {
        PodImport rampImport = exportRef.get();
        
        if (rampImport != null) {
          return rampImport;
        }
      }
      
      PodImport rampImport = new PodImport(sourceLoader, _targetLoader);
      _exportMap.put(sourceLoader, new SoftReference<>(rampImport));
      
      return rampImport;
    }
  }

  public String toString()
  {
    return getClass().getSimpleName() + "[" + _targetLoader + "]";
  }
}
