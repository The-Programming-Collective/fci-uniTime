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
package org.unitime.timetable.util;

import java.util.ArrayList;

import org.unitime.timetable.model.RefTableEntry;


/**
 * @author Dagmar Murray, Tomas Muller
 *
 */
public class ReferenceList extends ArrayList<RefTableEntry> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3760561970914801209L;

	public String labelForReference(String ref) {
		for (RefTableEntry r : this) {
			if (r.getReference().equals(ref)) 
				return r.getLabel();
		}
		return null;
	}
	
}
