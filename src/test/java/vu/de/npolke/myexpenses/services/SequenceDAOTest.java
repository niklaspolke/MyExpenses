package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Niklas Polke
 */
@Category(vu.de.npolke.myexpenses.InMemory.class)
public class SequenceDAOTest extends AbstractDAOTest {

	private static SequenceDAO sequenceDAO;

	private static long testCounter = 0;

	public SequenceDAOTest() {
		super("SequenceDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		sequenceDAO = (SequenceDAO) DAOFactory.getDAO(Long.class);
	}

	@Test
	public void getNextPrimaryKey() {
		long seq_value = sequenceDAO.getNextPrimaryKey();
		assertEquals(seq_value + 1, sequenceDAO.getNextPrimaryKey());
		assertEquals(seq_value + 2, sequenceDAO.getNextPrimaryKey());
		assertEquals(seq_value + 3, sequenceDAO.getNextPrimaryKey());
	}
}
