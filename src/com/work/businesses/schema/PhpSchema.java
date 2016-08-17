package com.work.businesses.schema;

import java.io.Serializable;

/**
 * 从php表中读取数据
 * 
 * @author tangbiao
 * 
 */
public class PhpSchema implements Serializable
{

    private static final long serialVersionUID = 201033331L;

    private String id = ""; // ID

    private String name = ""; // 名称
    
    private String age = ""; // 年龄

    private String sex = ""; // 性别

	public String getId()
    {

        return id;
    }

    public void setId(String id)
    {

        this.id = id;
    }

    public String getName()
    {

        return name;
    }

    public void setName(String name)
    {

        this.name = name;
    }

    public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
