/* AccessControlContext.java --- Access Control Context Class
 Copyright (C) 1999 Free Software Foundation, Inc.

 This file is part of GNU Classpath.

 GNU Classpath is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 GNU Classpath is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 02111-1307 USA.

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. */

package java.security;

/**
 * AccessControlContext makes system resource access decsion based on
 * permission rights.
 * 
 * It is used for a specific context and has only one method checkPermission.
 * It is similar to AccessController except that it makes decsions based on the
 * current context instead of the the current thread.
 * 
 * It is created by call AccessController.getContext method.
 * 
 * @author Mark Benvenuto
 * @since JDK 1.2
 */
public final class AccessControlContext {

    final Object vmContext;

    private final DomainCombiner combiner;

    /**
     * Construct a new AccessControlContext with the specified
     * ProtectionDomains. <code>context</code> must not be null and
     * duplicates will be removed.
     * 
     * @param context
     *            The ProtectionDomains to use
     */
    public AccessControlContext(ProtectionDomain[] context) {
        this.vmContext = createContext2(context);
        this.combiner = null;
    }

    private static native Object createContext2(ProtectionDomain[] context);

    /**
     * Construct a new AccessControlContext with the specified
     * ProtectionDomains. <code>context</code> must not be null and
     * duplicates will be removed.
     * 
     * @param context
     *            The context to use
     */
    AccessControlContext(Object context) {
        this.vmContext = context;
        this.combiner = null;
    }

    /**
     * Construct a new AccessControlContext with the specified
     * ProtectionDomains and DomainCombiner
     * 
     * @param acc
     *            The context to use
     * 
     * @since JDK 1.3
     */
    public AccessControlContext(AccessControlContext acc, DomainCombiner combiner) {
        this.vmContext = createContext(acc);
        this.combiner = combiner;
    }

    private static native Object createContext(AccessControlContext acc);

    /**
     * Returns the Domain Combiner associated with the AccessControlContext
     * 
     * @returns the DomainCombiner
     */
    public DomainCombiner getDomainCombiner() {
        return combiner;
    }

    /**
     * Determines whether or not the specific permission is granted depending
     * on the context it is within.
     * 
     * @param perm
     *            a permission to check
     * 
     * @throws AccessControlException
     *             if the permssion is not permitted
     */
    public native void checkPermission(Permission perm) throws AccessControlException;

    /**
     * Checks if two AccessControlContexts are equal.
     * 
     * It first checks if obj is an AccessControlContext class, and then checks
     * if each ProtectionDomain matches.
     * 
     * @param obj
     *            The object to compare this class to
     * 
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (obj instanceof AccessControlContext) {
            final AccessControlContext acc = (AccessControlContext) obj;
            return acc.vmContext.equals(vmContext);
        }
        return false;
    }

    /**
     * Computes a hash code of this class
     * 
     * @return a hash code representing this class
     */
    public int hashCode() {
        return vmContext.hashCode();
    }
    
    /**
     * @return Returns the vmContext.
     */
    final Object getVmContext() {
        return this.vmContext;
    }
}
