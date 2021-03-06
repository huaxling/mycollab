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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.module.project.ProjectContants;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectLinkBuilder {

	public static String generateProjectFullLink(Integer projectId,
			String prefixParam) {
		if (projectId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + prefixParam + "project/dashboard/"
				+ UrlEncodeDecoder.encode(projectId);
	}

	public static String generateProjectMemberFullLink(int projectId,
			String memberName) {
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/user/preview/"
				+ UrlEncodeDecoder.encode(projectId + "/" + memberName);
	}

	public static String generateBugPreviewFullLink(Integer projectId,
			Integer bugId) {
		if (projectId == null || bugId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateBugPreviewLink(projectId, bugId);
	}

	public static String generateMessagePreviewFullLink(Integer projectId,
			Integer messageId, String prefixParam) {
		if (projectId == null || messageId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ prefixParam
				+ ProjectLinkUtils.generateMessagePreviewLink(projectId,
						messageId);
	}

	public static String generateRiskPreviewFullLink(Integer projectId,
			Integer riskId) {
		if (projectId == null || riskId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/risk/preview/"
				+ UrlEncodeDecoder.encode(projectId + "/" + riskId);
	}

	public static String generateTaskPreviewFullLink(Integer projectId,
			Integer taskId) {
		if (projectId == null || taskId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskPreviewLink(projectId, taskId);
	}

	public static String generateTaskGroupPreviewFullLink(Integer projectId,
			Integer taskgroupId) {
		if (projectId == null || taskgroupId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskGroupPreviewLink(projectId,
						taskgroupId);
	}

	public static String generateMilestonePreviewFullLink(Integer projectId,
			Integer milestoneId) {
		if (projectId == null || milestoneId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateMilestonePreviewLink(projectId,
						milestoneId);
	}

	public static String generateProjectItemLink(int projectId, String type,
			int typeid) {
		String result = "";

		if (ProjectContants.PROJECT.equals(type)) {
		} else if (ProjectContants.MESSAGE.equals(type)) {
			result = ProjectLinkUtils.generateMessagePreviewLink(projectId,
					typeid);
		} else if (ProjectContants.MILESTONE.equals(type)) {
			result = ProjectLinkUtils.generateMilestonePreviewLink(projectId,
					typeid);
		} else if (ProjectContants.PROBLEM.equals(type)) {
			result = ProjectLinkUtils.generateProblemPreviewLink(projectId,
					typeid);
		} else if (ProjectContants.RISK.equals(type)) {
			result = ProjectLinkUtils.generateRiskPreview(projectId, typeid);
		} else if (ProjectContants.TASK.equals(type)) {
			result = ProjectLinkUtils
					.generateTaskPreviewLink(projectId, typeid);
		} else if (ProjectContants.TASK_LIST.equals(type)) {
			result = ProjectLinkUtils.generateTaskGroupPreviewLink(projectId,
					typeid);
		} else if (ProjectContants.BUG.equals(type)) {
			result = ProjectLinkUtils.generateBugPreviewLink(projectId, typeid);
		} else if (ProjectContants.BUG_COMPONENT.equals(type)) {
			result = ProjectLinkUtils.generateBugComponentPreviewLink(
					projectId, typeid);
		} else if (ProjectContants.BUG_VERSION.equals(type)) {
			result = ProjectLinkUtils.generateBugVersionPreviewLink(projectId,
					typeid);
		} else if (ProjectContants.STANDUP.equals(type)) {
			result = ProjectLinkUtils.generateStandUpPreviewLink();
		}

		return "#" + result;
	}
}
