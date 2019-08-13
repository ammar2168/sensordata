# alert-service
This service receives and processes co2 measurements from sensors and alerts when a co2 level reported by sensors reaches critical levels.

**Start the application**

Start the application from Application class `SensorDataApplication` which will bring up the Spring Boot application or simply run the jar file.

**API**

**Push sensor measurement**

To push a sensor measurement to the service

* **URL**

  api/v1/sensors/{uuid}/measurements


* **Method:**

  `POST`

* **Data Params**
   
   - name: co2 <br />
 	  description: This the co2 level in ppm <br />
 	  required: true <br />
 	  type: integer <br />

    - name: time <br />
      description: time at which the sensor emitted the data <br />
      required: true <br />
    	type: datatime <br />
 
         
* **Success Response:**
  * **Status Code:** 201 <br />
    **Content:** `{
    "id": "1"
  }`

* **Error Response:**
  * **Status Code:** 400 <br />
  **Content:** `{
    "errorMessage": "cO2 value must be provided"
  }`

OR

* **Status Code:** 400 <br />
    **Content:** `{
    "errorMessage":  "Time value must be provided"
  }`


OR 

  * **Status Code:** 400 <br />
    **Content:** `{
    "errorMessage": "Time cannot be empty or in wrong format"
  }`
  
 OR 
 
  * **Status Code:** 400 <br />
    **Content:** `{
    "errorMessage": "Invalid UUID : 3f8-4e76-b1-d7ca2a98e"
  }` 

* **Sample Call:**
```
POST /api/v1/sensors/7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e/mesurements 
HTTP/1.1
Host: localhost:8080
Content-Type: application/json
cache-control: no-cache
Postman-Token: 6132cad9-ae10-4f79-b6d5-1b86b4e9ddc3
	{
		
		"co2": 2001,
		"time": "2019-08-06T06:37:26.080004Z"
		
	}
```

**Sensor Status**

To get a sensor’s current status

* **URL**

  api/v1/sensors/{uuid}


* **Method:**

  `GET`

* **Data Params** <br />
        None
 
         
* **Success Response:**
  * **Status Code:** 200 <br />
    **Content:** `{
    "status": "ALERT"
}`

* **Error Response:**
  * **Status Code:** 400 <br />
    **Content:** `{
    "errorMessage": "Sensor uuid : 7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e not found"
  }`


* **Sample Call:**
```
GET /api/v1/sensors/7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e/ 
HTTP/1.1
Host: localhost:8080
cache-control: no-cache
Postman-Token: 1dbfc59b-4eaa-4975-a443-9f9f82b36ea7
```

**Get Sensor Metrics**

To get a sensors average and maximum co2 levels in last 30 days

* **URL**

  /api/v1/sensors/{uuid}/metrics


* **Method:**
`GET`

* **Data Params** <br />
        None
 
         
* **Success Response:**
  * **Status Code:** 200 <br />
    **Content:**   `{
    "maxLast30Days": "2001",
    "avgLast30Days": "2001.0000"
  }`


* **Error Response:**
  * **Status Code:** 400 <br />
  **Content:** `{
    "errorMessage": "Sensor uuid : 7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e not found"
  }`

* **Sample Call:**
```
GET /api/v1/sensors/7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e/ 
HTTP/1.1
Host: localhost:8080
cache-control: no-cache
Postman-Token: 1dbfc59b-4eaa-4975-a443-9f9f82b36ea7
```


**List Sensor alerts**

Description :
To get a list of a sensor’s alerts

* **URL**

  /api/v1/sensors/{uuid}/metrics


* **Method:**
`GET`

* **Data Params:** <br />
  None
 
         
* **Success Response:**

  * **Status Code:** 200 <br />
        **Content:** `[
		  {
		      "mesurement1":"2999", 
		      "mesurement2":"2999",
		      "mesurement3":"2999",
		      "startTime":"2019-08-06T06:57:26",
		      "endTime":"2019-08-06T06:57:26"
		  },
		  {
		      "mesurement1”:”3001”,
		      "mesurement2”:”3002”,
		      "mesurement3":"3003”,
		      "startTime":"2019-08-06T06:57:26",
		      "endTime":"2019-08-06T06:57:26"
		  }
  ]`
    
* **Error Response:**
  * **Status Code:** 400 <br />
  **Content:** `{
    "errorMessage": "Sensor id : 7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e not found"
}`

* **Sample Call:**
```
GET /api/v1/sensors/7ef7f906-b3f8-4e76-bfb1-c75d7ca2a98e/alerts
HTTP/1.1
Host: localhost:8080
cache-control: no-cache
Postman-Token: 1dbfc59b-4eaa-4975-a443-9f9f82b36ea7
```
