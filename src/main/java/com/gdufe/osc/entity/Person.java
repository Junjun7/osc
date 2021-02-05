package com.gdufe.osc.entity;

import com.gdufe.osc.utils.gson.SensitiveInfoMask;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.Max;

/**
 * @author changwenbo
 * @date 2020/9/18 17:52
 */
public class Person implements SensitiveInfoMask {

	private String name;

	@Max(100)
	private int age;

	public Person() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
