/*
 * Copyright (c) 1998-2015 Caucho Technology -- all rights reserved
 *
 * This file is part of Baratine(TM)(TM)
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

package io.baratine.service;


/**
 * Exception when the target service actor cannot recieve anymore messages due to a full mailbox.
 */
public class ServiceExceptionQueueFull extends ServiceException
{
  public ServiceExceptionQueueFull()
  {
  }
  
  public ServiceExceptionQueueFull(String msg)
  {
    super(msg);
  }
  
  public ServiceExceptionQueueFull(Throwable exn)
  {
    super(exn);
  }
  
  public ServiceExceptionQueueFull(String msg, Throwable exn)
  {
    super(msg, exn);
  }
  
  /**
   * Rethrows an exception to record the full stack trace, both caller
   * and callee.
   */
  @Override
  public ServiceExceptionQueueFull rethrow(String msg)
  {
    return new ServiceExceptionQueueFull(msg, this);
  }
}
