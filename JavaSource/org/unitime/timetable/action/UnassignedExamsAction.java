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
package org.unitime.timetable.action;

import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.tiles.annotation.TilesDefinition;
import org.apache.struts2.tiles.annotation.TilesPutAttribute;
import org.unitime.commons.Debug;
import org.unitime.commons.web.WebTable;
import org.unitime.localization.impl.Localization;
import org.unitime.localization.messages.ExaminationMessages;
import org.unitime.timetable.form.ExamChangesForm;
import org.unitime.timetable.form.ExamReportForm;
import org.unitime.timetable.model.BuildingPref;
import org.unitime.timetable.model.DepartmentStatusType;
import org.unitime.timetable.model.DistributionPref;
import org.unitime.timetable.model.ExamType;
import org.unitime.timetable.model.MidtermPeriodPreferenceModel;
import org.unitime.timetable.model.Exam;
import org.unitime.timetable.model.ExamPeriodPref;
import org.unitime.timetable.model.PeriodPreferenceModel;
import org.unitime.timetable.model.Preference;
import org.unitime.timetable.model.PreferenceLevel;
import org.unitime.timetable.model.RoomFeaturePref;
import org.unitime.timetable.model.RoomGroupPref;
import org.unitime.timetable.model.RoomPref;
import org.unitime.timetable.model.Session;
import org.unitime.timetable.model.dao.SessionDAO;
import org.unitime.timetable.security.rights.Right;
import org.unitime.timetable.solver.exam.ExamSolverProxy;
import org.unitime.timetable.solver.exam.ui.ExamInfo;
import org.unitime.timetable.util.ExportUtils;
import org.unitime.timetable.util.LookupTables;
import org.unitime.timetable.util.RoomAvailability;
import org.unitime.timetable.webutil.PdfWebTable;
import org.unitime.timetable.webutil.RequiredTimeTable;


/** 
 * @author Tomas Muller
 */
@Action(value = "unassignedExams", results = {
		@Result(name = "showReport", type = "tiles", location = "unassignedExams.tiles")
	})
@TilesDefinition(name = "unassignedExams.tiles", extend = "baseLayout", putAttributes =  {
		@TilesPutAttribute(name = "title", value = "Not-Assigned Examinations"),
		@TilesPutAttribute(name = "body", value = "/exam/unassigned.jsp"),
		@TilesPutAttribute(name = "showSolverWarnings", value = "exams")
	})
public class UnassignedExamsAction extends UniTimeAction<ExamReportForm> {
	private static final long serialVersionUID = 1476233244372449453L;
	protected static final ExaminationMessages MSG = Localization.create(ExaminationMessages.class);

	public String execute() throws Exception {
        // Check Access
		sessionContext.checkPermission(Right.NotAssignedExaminations);
		
		ExamSolverProxy solver = getExaminationSolverService().getSolver();

    	if (form == null) {
	    	form = new ExamChangesForm();
	    	form.reset();
	    	if (solver != null) form.setExamType(solver.getExamTypeId());
	    }
	    
    	if (form.getOp() != null) op = form.getOp();

    	if (MSG.actionExportPdf().equals(op) || MSG.actionExportCsv().equals(op) || MSG.buttonApply().equals(op)) {
            form.save(sessionContext);
        } else if (MSG.buttonRefresh().equals(op)) {
            form.reset();
            if (solver != null) form.setExamType(solver.getExamTypeId());
        }
        
        form.load(sessionContext);
        
        Session session = SessionDAO.getInstance().get(sessionContext.getUser().getCurrentAcademicSessionId());
        RoomAvailability.setAvailabilityWarning(request, session, form.getExamType(), true, false);
        
        Collection<ExamInfo> unassignedExams = null;
        if (form.getSubjectArea()!=null && form.getSubjectArea()!=0  && form.getExamType() != null) {
            if (solver!=null && solver.getExamTypeId().equals(form.getExamType()))
                unassignedExams = solver.getUnassignedExams(form.getSubjectArea());
            else
                unassignedExams = Exam.findUnassignedExams(sessionContext.getUser().getCurrentAcademicSessionId(), form.getSubjectArea(),form.getExamType());
        }
        
        WebTable.setOrder(sessionContext,"unassignedExams.ord",request.getParameter("ord"),1);
        
        WebTable table = getTable(true, false, unassignedExams);
        
        if (MSG.actionExportPdf().equals(op) && table!=null) {
        	ExportUtils.exportPDF(
        			getTable(false, true, unassignedExams),
        			WebTable.getOrder(sessionContext,"unassignedExams.ord"),
        			response, "unassigned");
        	return null;
        }

        if (MSG.actionExportCsv().equals(op) && table!=null) {
        	ExportUtils.exportCSV(
        			getTable(false, false, unassignedExams),
        			WebTable.getOrder(sessionContext,"unassignedExams.ord"),
        			response, "unassigned");
        	return null;
        }

        
        if (table!=null)
            form.setTable(table.printTable(WebTable.getOrder(sessionContext,"unassignedExams.ord")), 9, unassignedExams.size());

        if (request.getParameter("backId")!=null)
            request.setAttribute("hash", request.getParameter("backId"));
        
        LookupTables.setupExamTypes(request, sessionContext.getUser(), DepartmentStatusType.Status.ExamTimetable);
        
        return "showReport";
	}
	
    public PdfWebTable getTable(boolean html, boolean color, Collection<ExamInfo> exams) {
        if (exams==null || exams.isEmpty()) return null;
        boolean timeVertical = RequiredTimeTable.getTimeGridVertical(sessionContext.getUser());
        boolean timeText = RequiredTimeTable.getTimeGridAsText(sessionContext.getUser());
        
        String nl = (html?"<br>":"\n");
		PdfWebTable table =
            new PdfWebTable( 9,
                    MSG.sectionNotAssingedExaminations(), "unassignedExams.action?ord=%%",
                    new String[] {
                    		(form.getShowSections()?MSG.colOwner():MSG.colExamination()),
                    		MSG.colExamLength(),
                    		MSG.colSeatingType().replace("\n", nl),
                    		MSG.colExamSize(),
                    		MSG.colExamMaxRooms().replace("\n", nl),
                    		MSG.colInstructor(),
                    		MSG.colExamPeriodPrefs().replace("\n", nl),
                    		MSG.colExamRoomPrefs().replace("\n", nl),
                    		MSG.colExamDistributionPrefs().replace("\n", nl)},
       				new String[] {"left", "right", "center", "right", "right", "left", "left", "left", "left"},
       				new boolean[] {true, true, true, false, false, true, true, true, true} );
		table.setRowStyle("white-space:nowrap");
        try {
        	for (ExamInfo exam : exams) {
        	    String perPref = "", roomPref = "", distPref = "";
        	    
                if (html) {
                    roomPref += exam.getExam().getEffectivePrefHtmlForPrefType(RoomPref.class);
                    if (roomPref.length()>0) roomPref+=nl;
                    roomPref += exam.getExam().getEffectivePrefHtmlForPrefType(BuildingPref.class);
                    if (roomPref.length()>0) roomPref+=nl;
                    roomPref += exam.getExam().getEffectivePrefHtmlForPrefType(RoomFeaturePref.class);
                    if (roomPref.length()>0) roomPref+=nl;
                    roomPref += exam.getExam().getEffectivePrefHtmlForPrefType(RoomGroupPref.class);
                    if (roomPref.endsWith(nl)) roomPref = roomPref.substring(0, roomPref.length()-nl.length());
                    if (timeText) {
                        perPref += exam.getExam().getEffectivePrefHtmlForPrefType(ExamPeriodPref.class);
                    } else {
                        if (exam.getExam().getExamType().getType() == ExamType.sExamTypeMidterm) {
                            MidtermPeriodPreferenceModel epx = new MidtermPeriodPreferenceModel(exam.getExam().getSession(), exam.getExam().getExamType());
                            epx.load(exam.getExam());
                            perPref += epx.toString(true);
                        } else {
                            PeriodPreferenceModel px = new PeriodPreferenceModel(exam.getExam().getSession(), exam.getExamTypeId());
                            px.load(exam.getExam());
                            perPref = "<img border='0' src='pattern?v=" + (timeVertical ? 1 : 0) + "&x="+ exam.getExamId() +"' title='"+px.toString()+"'>";
                        }
                    }
                    distPref += exam.getExam().getEffectivePrefHtmlForPrefType(DistributionPref.class);
                } else {
                    for (Iterator j=exam.getExam().effectivePreferences(RoomPref.class).iterator();j.hasNext();) {
                        Preference pref = (Preference)j.next();
                        if (roomPref.length()>0) roomPref+=nl;
                        roomPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") + pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
                    }
                    for (Iterator j=exam.getExam().effectivePreferences(BuildingPref.class).iterator();j.hasNext();) {
                        Preference pref = (Preference)j.next();
                        if (roomPref.length()>0) roomPref+=nl;
                        roomPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") + pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
                    }
                    for (Iterator j=exam.getExam().effectivePreferences(RoomFeaturePref.class).iterator();j.hasNext();) {
                        Preference pref = (Preference)j.next();
                        if (roomPref.length()>0) roomPref+=nl;
                        roomPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") + pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
                    }
                    for (Iterator j=exam.getExam().effectivePreferences(RoomGroupPref.class).iterator();j.hasNext();) {
                        Preference pref = (Preference)j.next();
                        if (roomPref.length()>0) roomPref+=nl;
                        roomPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") + pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
                    }
                    if (ExamType.sExamTypeMidterm==exam.getExamType().getType()) {
                        MidtermPeriodPreferenceModel epx = new MidtermPeriodPreferenceModel(exam.getExam().getSession(), exam.getExamType());
                        epx.load(exam.getExam());
                        perPref+=epx.toString(false, true);
                    } else {
                    	if (timeText || !color) {
    						for (Iterator j=exam.getExam().effectivePreferences(ExamPeriodPref.class).iterator();j.hasNext();) {
    	                        Preference pref = (Preference)j.next();
    	                        if (perPref.length()>0) perPref+=nl;
    	                        perPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") +pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
    						}
                    	} else {
                            PeriodPreferenceModel px = new PeriodPreferenceModel(exam.getExam().getSession(), exam.getExamType().getUniqueId());
                            px.load(exam.getExam());
                            RequiredTimeTable rtt = new RequiredTimeTable(px);
                            Image image = rtt.createBufferedImage(timeVertical);
        					if (image != null) {
        						table.addImage(exam.getExamId().toString(), image);
        						perPref += "@@IMAGE "+exam.getExamId().toString()+" ";
        					} else {
        						for (Iterator j=exam.getExam().effectivePreferences(ExamPeriodPref.class).iterator();j.hasNext();) {
        	                        Preference pref = (Preference)j.next();
        	                        if (perPref.length()>0) perPref+=nl;
        	                        perPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") +pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText();
        						}
                            }
                    	}
                    }
                    for (Iterator j=exam.getExam().effectivePreferences(DistributionPref.class).iterator();j.hasNext();) {
                        DistributionPref pref = (DistributionPref)j.next();
                        if (distPref.length()>0) distPref+=nl;
                        distPref += (color ? "@@COLOR " + PreferenceLevel.prolog2color(pref.getPrefLevel().getPrefProlog()) + " " : "") + pref.getPrefLevel().getAbbreviation()+" "+pref.preferenceText(true, true, " (", ", ",")").replaceAll("&lt;","<").replaceAll("&gt;",">");
                    }
                }
                
                String instructors = exam.getInstructorName(", ");
                
        	    table.addLine(
        	            "onClick=\"showGwtDialog('" + MSG.dialogExamAssign() + "', 'examInfo.action?examId="+exam.getExamId()+"','900','90%');\"",
                        new String[] {
                            (html?"<a name='"+exam.getExamId()+"'>":"")+(form.getShowSections()?exam.getSectionName(nl):exam.getExamName())+(html?"</a>":""),
                            String.valueOf(exam.getLength()),
                            (Exam.sSeatingTypeNormal==exam.getSeatingType()?MSG.seatingNormal():MSG.seatingExam()),
                            String.valueOf(exam.getNrStudents()),
                            String.valueOf(exam.getMaxRooms()),
                            instructors,
                            perPref,
                            roomPref,
                            distPref
                        },
                        new Comparable[] {
                            exam,
                            exam.getLength(),
                            exam.getSeatingType(),
                            exam.getNrStudents(),
                            exam.getMaxRooms(),
                            instructors,
                            perPref,
                            roomPref,
                            distPref
                        },
                        exam.getExamId().toString());
        	}
        } catch (Exception e) {
        	Debug.error(e);
        	table.addLine(new String[] {"<font color='red'>"+MSG.error(e.getMessage())+"</font>"},null);
        }
        return table;
    }	
}

