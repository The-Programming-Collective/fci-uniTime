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
package org.unitime.timetable.form;

import javax.servlet.http.HttpServletRequest;

import org.unitime.timetable.action.UniTimeAction;

/** 
 * @author Tomas Muller
 */
public class LastChangesForm implements UniTimeForm {
	private static final long serialVersionUID = 3633681949556250656L;
	private String iOp;
    private int iN;
    private Long iDepartmentId, iSubjAreaId, iManagerId;

    @Override
	public void reset() {
		iOp = null; 
        iN = 100;
        iDepartmentId = Long.valueOf(-1);
        iSubjAreaId = Long.valueOf(-1);
        iManagerId = Long.valueOf(-1);
	}
	
	@Override
	public void validate(UniTimeAction action) {
		
	}

	public String getOp() { return iOp; }
	public void setOp(String op) { iOp = op; }
    public int getN() { return iN; }
    public void setN(int n) { iN = n; }
    public Long getDepartmentId() { return iDepartmentId; }
    public void setDepartmentId(Long departmentId) { iDepartmentId = departmentId; }
    public Long getSubjAreaId() { return iSubjAreaId; }
    public void setSubjAreaId(Long subjAreaId) { iSubjAreaId = subjAreaId; }
    public Long getManagerId() { return iManagerId; }
    public void setManagerId(Long managerId) { iManagerId = managerId; }
    
    public void load(HttpServletRequest request) {
        Integer n = (Integer)request.getSession().getAttribute("LastChanges.N");
        setN(n==null?100:n.intValue());
        setDepartmentId((Long)request.getSession().getAttribute("LastChanges.DepartmentId"));
        setSubjAreaId((Long)request.getSession().getAttribute("LastChanges.SubjAreaId"));
        setManagerId((Long)request.getSession().getAttribute("LastChanges.ManagerId"));
    }
    
    public void save(HttpServletRequest request) {
        request.getSession().setAttribute("LastChanges.N", Integer.valueOf(getN()));
        if (getDepartmentId()==null)
            request.getSession().removeAttribute("LastChanges.DepartmentId");
        else
            request.getSession().setAttribute("LastChanges.DepartmentId", getDepartmentId());
        if (getSubjAreaId()==null)
            request.getSession().removeAttribute("LastChanges.SubjAreaId");
        else
            request.getSession().setAttribute("LastChanges.SubjAreaId", getSubjAreaId());
        if (getManagerId()==null)
            request.getSession().removeAttribute("LastChanges.ManagerId");
        else
            request.getSession().setAttribute("LastChanges.ManagerId", getManagerId());
    }

}

