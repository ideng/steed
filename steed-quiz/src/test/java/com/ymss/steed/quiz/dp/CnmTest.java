package com.ymss.steed.quiz.dp;

import org.junit.Test;

import com.ymss.steed.quiz.search.Cnm;

public class CnmTest {
	@Test
	public void test()
	{
		Cnm cnm = new Cnm();
		cnm.cnm(new int[]{1,2,3,4,5}, 3);
	}
}
