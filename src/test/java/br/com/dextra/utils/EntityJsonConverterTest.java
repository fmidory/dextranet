package br.com.dextra.utils;

import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class EntityJsonConverterTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void entityJsonConverterTest() {
		Entity entity = dadoUmaEntity();
		String json = quandoEuConvertoPraUmJson(entity);
		entaoDeemUmNomeMelhor(json);
	}

	private void entaoDeemUmNomeMelhor(String json) {
		Assert.assertTrue(json.contains("valorDeTeste"));
		Assert.assertTrue(json.contains("Gabriel"));

	}

	private String quandoEuConvertoPraUmJson(Entity entity) {
		return EntityJsonConverter.toJson(entity).toString();
	}

	private Entity dadoUmaEntity() {
		Entity entity = new Entity("Teste");
		entity.setProperty("teste", "valorDeTeste");
		entity.setProperty("nome", "Gabriel");
		return entity;
	}
}
