/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/
package org.unitime.timetable.model;



import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import org.unitime.timetable.model.base.BaseSolverParameter;



/**
 * @author Tomas Muller
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, include = "non-lazy")
@Table(name = "solver_parameter")
public class SolverParameter extends BaseSolverParameter implements Comparable {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SolverParameter () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SolverParameter (java.lang.Long uniqueId) {
		super(uniqueId);
	}

/*[CONSTRUCTOR MARKER END]*/

    public int compareTo(Object o) {
        if (o==null || !(o instanceof SolverParameter)) return -1;
        SolverParameter p = (SolverParameter)o;
        int cmp = getDefinition().compareTo(p.getDefinition());
        if (cmp!=0) return cmp;
        return (getUniqueId() == null ? Long.valueOf(-1) : getUniqueId()).compareTo(p.getUniqueId() == null ? -1 : p.getUniqueId());
    }

}
