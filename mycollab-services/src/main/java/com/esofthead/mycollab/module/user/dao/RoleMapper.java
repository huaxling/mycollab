/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.dao;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper extends ICrudGenericDAO {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int countByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int deleteByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    List<Role> selectByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    Integer insertAndReturnKey(Role value);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    void removeKeysWithSession(List primaryKeys);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_roles
     *
     * @mbggenerated Thu Oct 31 11:24:43 ICT 2013
     */
    void massUpdateWithSession(@Param("record") Role record, @Param("primaryKeys") List primaryKeys);
}