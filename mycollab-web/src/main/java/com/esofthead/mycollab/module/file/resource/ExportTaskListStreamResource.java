/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.file.resource;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.reporting.BeanDataSource;
import com.esofthead.mycollab.reporting.ColumnFieldComponentBuilder;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleColumnComponentBuilderMap;
import com.esofthead.mycollab.reporting.TableViewFieldDecorator;
import com.esofthead.mycollab.reporting.Templates;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ExportTaskListStreamResource<T, S extends SearchCriteria> extends
		ExportItemsStreamResource<SimpleTaskList> {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(ExportTaskListStreamResource.class);

	private ISearchableService searchService;
	private S searchCriteria;

	protected RpParameterBuilder parameters;

	public ExportTaskListStreamResource(String reportTitle,
			ReportExportType outputForm, ISearchableService searchService,
			S searchCriteria, RpParameterBuilder parameters) {
		super(reportTitle, outputForm);
		this.searchCriteria = searchCriteria;
		this.searchService = searchService;
		List<TableViewField> fields = Arrays.asList(TaskTableFieldDef.taskname,
				TaskTableFieldDef.startdate, TaskTableFieldDef.dueDate,
				TaskTableFieldDef.complete, TaskTableFieldDef.assignUser);
		this.parameters = new RpParameterBuilder(fields);
	}

	public interface TaskTableFieldDef {
		public static TableViewField selected = new TableViewField("",
				"selected", UIConstants.TABLE_CONTROL_WIDTH);

		public static TableViewField taskname = new TableViewField("Task Name",
				"taskname", UIConstants.TABLE_X_LABEL_WIDTH);

		public static TableViewField startdate = new TableViewField(
				"Start Date", "startdate", UIConstants.TABLE_DATE_WIDTH);

		public static TableViewField dueDate = new TableViewField("Due Date",
				"deadline", UIConstants.TABLE_DATE_WIDTH);

		public static TableViewField complete = new TableViewField(
				"% Complete", "percentagecomplete",
				UIConstants.TABLE_S_LABEL_WIDTH);

		public static TableViewField assignUser = new TableViewField(
				"Assignee", "assignuser", UIConstants.TABLE_X_LABEL_WIDTH);
	}

	@Override
	protected void initReport() throws Exception {
		SearchRequest<S> searchRequest = new SearchRequest<S>(searchCriteria,
				0, Integer.MAX_VALUE);
		List<SimpleTaskList> lstSimleTaskList = searchService
				.findPagableListByCriteria(searchRequest);

		for (SimpleTaskList taskList : lstSimleTaskList) {
			VerticalListBuilder componetBuilder = cmp.verticalList();
			StyleBuilder style = stl.style(Templates.bold12TitleStyle)
					.setBorder(stl.penThin());

			StyleBuilder styleHyperLink = stl.style(Templates.bold12TitleStyle)
					.setBorder(stl.penThin()).setUnderline(true);

			ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
					taskList.getProjectid());
			String phaseHyperLink = linkGenerator
					.generateMilestonePreviewFullLink(taskList.getMilestoneid());

			HorizontalListBuilder taskGroupLabel = cmp.horizontalList();
			taskGroupLabel.add(cmp.text("Task group: ").setFixedWidth(50)
					.setStyle(Templates.columnTitleStyle));

			// TaskList Name
			StyleBuilder taskGroupStyle = stl
					.style(Templates.boldStyle)
					.setUnderline(true)
					.setFontSize(12)
					.setAlignment(HorizontalAlignment.CENTER,
							VerticalAlignment.MIDDLE);
			TextFieldBuilder<String> taskListNameHeader = cmp
					.text(taskList.getName())
					.setFixedWidth(1116)
					.setHorizontalAlignment(HorizontalAlignment.CENTER)
					.setHyperLink(
							hyperLink(linkGenerator
									.generateTaskGroupPreviewFullLink(taskList
											.getId())))
					.setStyle(taskGroupStyle);
			taskGroupLabel.add(taskListNameHeader).setStyle(
					Templates.columnTitleStyle);

			// label
			log.debug("Label value : " + taskList.getDescription());
			TextFieldBuilder<String> desLabel = cmp.text("Desciption :")
					.setStyle(style).setFixedWidth(150);
			TextFieldBuilder<String> descriptText = cmp
					.text(taskList.getDescription()).setFixedWidth(1020)
					.setStyle(style);
			HorizontalListBuilder deshorizontal = cmp.horizontalList();
			deshorizontal.add(desLabel).add(descriptText);

			// Assignee
			log.debug("Assignee value : " + taskList.getOwnerFullName());
			TextFieldBuilder<String> assigneeLbl = cmp.text("Assignee :")
					.setStyle(style).setFixedWidth(150);
			TextFieldBuilder<String> assignee = cmp
					.text(taskList.getOwnerFullName()).setStyle(style)
					.setFixedWidth(435);

			TextFieldBuilder<String> phaseLbl = cmp.text("Phase name :")
					.setStyle(style).setFixedWidth(150);

			TextFieldBuilder<String> phase = cmp
					.text(taskList.getMilestoneName())
					.setHyperLink(hyperLink(phaseHyperLink))
					.setStyle(Templates.underlineStyle)
					.setStyle(styleHyperLink).setFixedWidth(435);

			HorizontalListBuilder assingeeAndPhaseHorizontal = cmp
					.horizontalList();
			assingeeAndPhaseHorizontal.add(assigneeLbl).add(assignee)
					.add(phaseLbl).add(phase);

			// progress
			log.debug("Progress start ----");
			TextFieldBuilder<String> progressLbl = cmp.text("Progress :")
					.setStyle(style).setFixedWidth(150);

			DecimalFormat df = new DecimalFormat("#");
			df.setRoundingMode(RoundingMode.HALF_EVEN);

			TextFieldBuilder<String> progress = cmp
					.text(df.format(taskList.getPercentageComplete()) + "%")
					.setStyle(style).setFixedWidth(435);

			TextFieldBuilder<String> numberTaskLbl = cmp
					.text("Number of open tasks :").setStyle(style)
					.setFixedWidth(150);

			TextFieldBuilder<String> taskNumText = cmp
					.text("(" + taskList.getNumOpenTasks() + "/"
							+ taskList.getNumAllTasks() + ")")
					.setFixedWidth(435).setStyle(style);

			HorizontalListBuilder horizontalOfProgressAndNumberTask = cmp
					.horizontalList();

			horizontalOfProgressAndNumberTask.add(progressLbl).add(progress)
					.add(numberTaskLbl).add(taskNumText);

			// Add to Vertical List Builder -------
			componetBuilder.add(taskGroupLabel).add(deshorizontal)
					.add(assingeeAndPhaseHorizontal)
					.add(horizontalOfProgressAndNumberTask);
			SimpleTaskJasperReportBuilder subReportBuilder = new SimpleTaskJasperReportBuilder(
					taskList.getSubTasks(), parameters);
			if (taskList.getSubTasks() != null
					&& taskList.getSubTasks().size() > 0) {
				componetBuilder.add(subReportBuilder.getSubreportBuilder());
			}
			componetBuilder.add(cmp.horizontalList().setHeight(7));
			reportBuilder.addDetail(componetBuilder);
		}
	}

	private static class SimpleTaskJasperReportBuilder {

		private BeanDataSource dataSource;
		private RpParameterBuilder parameters;

		public SimpleTaskJasperReportBuilder(List data,
				RpParameterBuilder parameters) {
			this.dataSource = new BeanDataSource(data);
			this.parameters = parameters;
		}

		public ComponentBuilder getSubreportBuilder() {
			HorizontalListBuilder horizontalBuilder = cmp.horizontalList();
			horizontalBuilder.setStyle(stl.style(Templates.boldCenteredStyle)
					.setBorder(stl.penThin()));
			SubreportBuilder subreport = cmp.subreport(
					new SimpleTaskExpression()).setDataSource(dataSource);
			horizontalBuilder.add(subreport);
			return horizontalBuilder;
		}

		private class SimpleTaskExpression extends
				AbstractSimpleExpression<JasperReportBuilder> {
			private static final long serialVersionUID = 1L;

			@Override
			public JasperReportBuilder evaluate(ReportParameters param) {
				JasperReportBuilder report = report();
				report.setTemplate(Templates.reportTemplate);

				Field[] clsFields = ClassUtils.getAllFields(Task.class);
				for (Field objField : clsFields) {
					DRIDataType<Object, ? extends Object> jrType = null;
					try {
						jrType = type.detectType(objField.getType().getName());
					} catch (DRException e) {
						throw new MyCollabException(e);
					}
					report.addField(objField.getName(), jrType);
				}

				List<TableViewFieldDecorator> fields = parameters.getFields();

				List<? extends ColumnFieldComponentBuilder> lstFieldBuilder = SimpleColumnComponentBuilderMap
						.getListFieldBuilder(Task.class);
				// build columns of report
				for (TableViewFieldDecorator field : fields) {

					log.debug("Inject renderer if any");
					if (lstFieldBuilder != null) {
						for (int i = lstFieldBuilder.size() - 1; i >= 0; i--) {
							ColumnFieldComponentBuilder fieldBuilder = lstFieldBuilder
									.get(i);
							if (field.getField().equals(
									fieldBuilder.getFieldName())) {
								field.setFieldComponentExpression(fieldBuilder
										.getDriExpression());
								field.setComponentBuilder(fieldBuilder
										.getComponentBuilder());
							}
						}
					}
					log.debug("Construct component builder {} and width {}",
							field.getField(), field.getDefaultWidth());
					ComponentColumnBuilder columnBuilder = col.componentColumn(
							field.getDesc(), field.getComponentBuilder())
							.setWidth(field.getDefaultWidth());

					report.addColumn(columnBuilder);
				}
				return report;
			}
		}
	}

	@Override
	protected void fillReport() {
		reportBuilder.setDataSource(new JREmptyDataSource());
	}
}
