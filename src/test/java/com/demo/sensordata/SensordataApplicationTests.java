package com.demo.sensordata;

import com.demo.sensordata.entities.Sensor;
import com.demo.sensordata.model.SensorInput;
import com.demo.sensordata.repository.AlertRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SensordataApplicationTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String getRootUrl() {
		return "http://localhost:" + 8080;
	}

	private ResponseEntity<String> createMeasurement(SensorInput sensorInput) {
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> postResponse = restTemplate.postForEntity(
				getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0/measurements", sensorInput,
				String.class);
		return postResponse;
	}

	@Test
	public void testCreateMesurements() throws JSONException {
		SensorInput sensorInput = new SensorInput(1000, "2019-08-09T18:12:47");
		ResponseEntity<String> response = createMeasurement(sensorInput);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatusCodeValue() == 201);
		Assert.assertNotNull(response.getBody());
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyId = keys.next();
		Assert.assertEquals(keyId, "id");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testSensorStatusOK() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(1000, "2019-08-09T19:12:47");
		createMeasurement(sensorInput1);
		SensorInput sensorInput2 = new SensorInput(1001, "2019-08-09T19:13:47");
		createMeasurement(sensorInput2);
		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		Assert.assertEquals(jsonObj.get("status"), "OK");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testSensorStatusWARN() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput = new SensorInput(2001, "2019-08-09T18:16:47");
		createMeasurement(sensorInput);
		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		Assert.assertEquals(jsonObj.get("status"), "WARN");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testSensorStatusALERT() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(2001, "2019-08-09T20:12:47");
		createMeasurement(sensorInput1);
		SensorInput sensorInput2 = new SensorInput(2002, "2019-08-09T21:13:47");
		createMeasurement(sensorInput2);
		SensorInput sensorInput3 = new SensorInput(2003, "2019-08-09T22:14:47");
		createMeasurement(sensorInput3);
		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		Assert.assertEquals(jsonObj.get("status"), "ALERT");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testSensorStatusFromOkToWARN() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		// first create measurement for OK status
		SensorInput sensorInput1 = new SensorInput(1000, "2019-08-09T18:12:47");
		createMeasurement(sensorInput1);
		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);

		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		// verify OK status
		Assert.assertEquals(jsonObj.get("status"), "OK");

		// create measurement for WARN status
		SensorInput sensorInput2 = new SensorInput(2001, "2019-08-09T18:16:47");
		createMeasurement(sensorInput2);
		ResponseEntity<String> statusResponse2 = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse2);
		Assert.assertTrue(statusResponse2.getStatusCodeValue() == 200);

		JSONObject jsonObj2 = new JSONObject(statusResponse2.getBody());
		Iterator<String> keys2 = jsonObj2.keys();
		String keyStatus2 = keys2.next();
		Assert.assertEquals(keyStatus2, "status");
		// verify WARN status
		Assert.assertEquals(jsonObj2.get("status"), "WARN");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");
	}

	@Test
	public void testSensorStatusFromWarnToOk() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		// create measurement for WARN status
		SensorInput sensorInput1 = new SensorInput(2071, "2019-08-09T18:12:47");
		createMeasurement(sensorInput1);

		ResponseEntity<String> status = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(status);
		Assert.assertTrue(status.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(status.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		// verify WARN status
		Assert.assertEquals(jsonObj.get("status"), "WARN");

		// create measurements for OK status
		SensorInput sensorInput2 = new SensorInput(1001, "2019-08-09T18:13:47");
		createMeasurement(sensorInput2);
		SensorInput sensorInput3 = new SensorInput(1002, "2019-08-09T18:14:47");
		createMeasurement(sensorInput3);
		SensorInput sensorInput4 = new SensorInput(1003, "2019-08-09T18:16:47");
		createMeasurement(sensorInput4);

		ResponseEntity<String> status2 = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(status2);
		Assert.assertTrue(status2.getStatusCodeValue() == 200);
		JSONObject jsonObj2 = new JSONObject(status2.getBody());
		Iterator<String> keys2 = jsonObj2.keys();
		String keyStatus2 = keys2.next();
		Assert.assertEquals(keyStatus2, "status");
		Assert.assertEquals(jsonObj2.get("status"), "OK");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testSensorStatusFromWarnToAlert() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(2001, "2019-08-09T20:12:47");
		createMeasurement(sensorInput1);
		ResponseEntity<String> status = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(status);
		Assert.assertTrue(status.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(status.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		// verify WARN status
		Assert.assertEquals(jsonObj.get("status"), "WARN");

		// create measurements for alert
		SensorInput sensorInput2 = new SensorInput(2001, "2019-08-09T20:12:47");
		createMeasurement(sensorInput2);
		SensorInput sensorInput3 = new SensorInput(2002, "2019-08-09T20:13:47");
		createMeasurement(sensorInput3);

		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj2 = new JSONObject(statusResponse.getBody());
		Iterator<String> keys2 = jsonObj2.keys();
		String keyStatus2 = keys2.next();
		Assert.assertEquals(keyStatus2, "status");
		Assert.assertEquals(jsonObj2.get("status"), "ALERT");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");
	}

	@Test
	public void testSensorStatusFromOkToAlert() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();

		// measurement for status OK
		SensorInput sensorInput1 = new SensorInput(1000, "2019-08-09T20:12:47");
		createMeasurement(sensorInput1);
		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		// verify status OK
		Assert.assertEquals(jsonObj.get("status"), "OK");

		// create measurements for alerts
		SensorInput sensorInput2 = new SensorInput(2050, "2019-08-09T20:15:47");
		createMeasurement(sensorInput2);
		SensorInput sensorInput3 = new SensorInput(2051, "2019-08-09T20:16:47");
		createMeasurement(sensorInput3);
		SensorInput sensorInput4 = new SensorInput(2052, "2019-08-09T20:17:47");
		createMeasurement(sensorInput4);
		ResponseEntity<String> response2 = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(response2);
		Assert.assertTrue(response2.getStatusCodeValue() == 200);
		JSONObject jsonObj2 = new JSONObject(response2.getBody());
		Iterator<String> keys2 = jsonObj2.keys();
		String keyStatus2 = keys2.next();
		Assert.assertEquals(keyStatus2, "status");
		Assert.assertEquals(jsonObj2.get("status"), "ALERT");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");
	}

	@Test
	public void testSensorStatusFromAlertToOk() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(2001, "2019-08-09T20:12:47");
		createMeasurement(sensorInput1);
		SensorInput sensorInput2 = new SensorInput(2002, "2019-08-09T20:13:47");
		createMeasurement(sensorInput2);
		SensorInput sensorInput3 = new SensorInput(2003, "2019-08-09T20:14:47");
		createMeasurement(sensorInput3);

		ResponseEntity<String> statusResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse);
		Assert.assertTrue(statusResponse.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(statusResponse.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyStatus = keys.next();
		Assert.assertEquals(keyStatus, "status");
		Assert.assertEquals(jsonObj.get("status"), "ALERT");

		SensorInput sensorInput4 = new SensorInput(1001, "2019-08-09T20:15:47");
		createMeasurement(sensorInput4);
		SensorInput sensorInput5 = new SensorInput(1200, "2019-08-09T20:16:47");
		createMeasurement(sensorInput5);
		SensorInput sensorInput6 = new SensorInput(1400, "2019-08-09T20:17:47");
		createMeasurement(sensorInput6);
		ResponseEntity<String> statusResponse2 = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0", String.class);
		Assert.assertNotNull(statusResponse2);
		Assert.assertTrue(statusResponse2.getStatusCodeValue() == 200);
		JSONObject jsonObj2 = new JSONObject(statusResponse2.getBody());
		Iterator<String> keys2 = jsonObj2.keys();
		String keyStatus2 = keys2.next();
		Assert.assertEquals(keyStatus2, "status");
		Assert.assertEquals(jsonObj2.get("status"), "OK");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");
	}

	@Test
	public void testListSensorAlerts() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(2001, "2019-08-09T18:12:47");
		SensorInput sensorInput2 = new SensorInput(2002, "2019-08-09T18:13:47");
		SensorInput sensorInput3 = new SensorInput(2003, "2019-08-09T18:14:47");
		createMeasurement(sensorInput1);
		createMeasurement(sensorInput2);
		createMeasurement(sensorInput3);

		ResponseEntity<String> alertsResponse = restTemplate.getForEntity(
				getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0/alerts", String.class);

		JSONArray jsonArr = new JSONArray(alertsResponse.getBody());
		JSONObject jsonObj = (JSONObject) jsonArr.get(0);
		Assert.assertNotNull(alertsResponse);
		Assert.assertTrue(alertsResponse.getStatusCodeValue() == 200);

		Iterator<String> keys = jsonObj.keys();
		String keyStartTime = keys.next();
		keys.hasNext();
		String keyEndTime = keys.next();
		keys.hasNext();
		String keyMeasurement1 = keys.next();
		keys.hasNext();
		String keyMeasurement2 = keys.next();
		keys.hasNext();
		String keyMeasurement3 = keys.next();

		// check if all expected keys are present
		Assert.assertEquals(keyStartTime, "startTime");
		Assert.assertEquals(keyEndTime, "endTime");
		Assert.assertEquals(keyMeasurement1, "measurement1");
		Assert.assertEquals(keyMeasurement2, "measurement2");
		Assert.assertEquals(keyMeasurement3, "measurement3");

		// check if keys have expected values
		Assert.assertEquals(jsonObj.get("startTime"), "2019-08-09T18:12:47");
		Assert.assertEquals(jsonObj.get("endTime"), "2019-08-09T18:14:47");
		Assert.assertEquals(jsonObj.get("measurement1"), "2001");
		Assert.assertEquals(jsonObj.get("measurement2"), "2002");
		Assert.assertEquals(jsonObj.get("measurement3"), "2003");

		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");
	}

	/**
	 * Value = 2003,2004,2005,2006,2007 Check status = {
	 *
	 * "maxLast30Days": "2007",
	 *
	 * "avgLast30Days": "2005.0000"
	 *
	 * }
	 * 
	 * @throws JSONException
	 */
	@Test
	public void testSensorLastThirtyDaysMaxandAvg() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		SensorInput sensorInput1 = new SensorInput(2003, "2019-08-09T18:12:47");
		SensorInput sensorInput2 = new SensorInput(2004, "2019-08-09T18:13:47");
		SensorInput sensorInput3 = new SensorInput(2005, "2019-08-09T18:15:47");
		SensorInput sensorInput4 = new SensorInput(2006, "2019-08-09T18:15:47");
		SensorInput sensorInput5 = new SensorInput(2007, "2019-08-09T18:15:47");
		createMeasurement(sensorInput1);
		createMeasurement(sensorInput2);
		createMeasurement(sensorInput3);
		createMeasurement(sensorInput4);
		createMeasurement(sensorInput5);

		ResponseEntity<String> response = restTemplate.getForEntity(
				getRootUrl() + "/api/v1/sensors/3dd4fa6e-2899-4429-b818-d34fe8df5dd0/metrics", String.class);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatusCodeValue() == 200);

		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String key1 = keys.next();
		keys.hasNext();
		String key2 = keys.next();

		// check if expected keys exists
		Assert.assertEquals(key1, "maxLast30Days");
		Assert.assertEquals(key2, "avgLast30Days");
		// check for expected values
		Assert.assertEquals(jsonObj.get("maxLast30Days"), "2007");
		Assert.assertEquals(jsonObj.get("avgLast30Days"), "2005.0000");
		deleteTestData("3dd4fa6e-2899-4429-b818-d34fe8df5dd0");

	}

	@Test
	public void testInvalidSensorIdInMetricsAPI() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		String sensorId = "test-" + System.currentTimeMillis();
		// check metrics API with unknown sensor Id
		ResponseEntity<String> response = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/" + sensorId + "/metrics", String.class);
		Assert.assertTrue(response.getStatusCodeValue() != 200);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyErrorMessage = keys.next();
		Assert.assertEquals(keyErrorMessage, "errorMessage");
		Assert.assertEquals(jsonObj.get("errorMessage"), "Sensor uuid: " + sensorId + " not found");

	}

	@Test
	public void testInvalidSensorIdInListAlertsAPI() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		String sensorId = "test-" + System.currentTimeMillis();
		// check list alerts API with unknown sensor Id
		ResponseEntity<String> response = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/" + sensorId + "/alerts", String.class);
		Assert.assertTrue(response.getStatusCodeValue() != 200);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyErrorMessage = keys.next();
		Assert.assertEquals(keyErrorMessage, "errorMessage");
		Assert.assertEquals(jsonObj.get("errorMessage"), "Sensor uuid: " + sensorId + " not found");

	}

	@Test
	public void testInvalidSensorIdInGetSensorStatusAPI() throws JSONException {
		TestRestTemplate restTemplate = new TestRestTemplate();
		// check sensor status API with unknown sensor Id
		String sensorId = "test-" + System.currentTimeMillis();
		ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl() + "/api/v1/sensors/" + sensorId,
				String.class);
		Assert.assertTrue(response.getStatusCodeValue() != 200);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyErrorMessage = keys.next();
		Assert.assertEquals(keyErrorMessage, "errorMessage");
		Assert.assertEquals(jsonObj.get("errorMessage"), "Sensor uuid: " + sensorId + " not found");

	}

	@Test
	public void testEmptyTimeValueInCreateMeasurement() throws JSONException {
		// test create measurement API with empty time
		SensorInput sensorInput = new SensorInput(2003, "");
		ResponseEntity<String> response = createMeasurement(sensorInput);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyErrorMessage = keys.next();
		Assert.assertEquals(keyErrorMessage, "errorMessage");
		Assert.assertEquals(jsonObj.get("errorMessage"), "Time cannot be empty or in wrong format");
	}

	@Test
	public void testInvalidTimeValueInCreateMeasurement() throws JSONException {
		// test create measurement API with wrong time value
		SensorInput sensorInput = new SensorInput(2003, "ah^&4s966");
		ResponseEntity<String> response = createMeasurement(sensorInput);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Iterator<String> keys = jsonObj.keys();
		String keyErrorMessage = keys.next();
		Assert.assertEquals(keyErrorMessage, "errorMessage");
		Assert.assertEquals(jsonObj.get("errorMessage"), "Time cannot be empty or in wrong format");

	}

	/**
	 * 
	 * @param uuid
	 */
	public void deleteTestData(String uuid) {
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> postResponse = restTemplate
				.getForEntity(getRootUrl() + "/api/v1/sensors/" + uuid + "/delete-sensor-data", String.class);
	}

}
