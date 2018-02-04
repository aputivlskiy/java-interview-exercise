# Solution

Simple REST API service implemented with SpringBoot. Uses in-memory H2 DB for persistence.

* [Usage](#usage)
* [API Endpoints](#api)
  * [Get all job offers](#listjobs)
  * [Get job offer](#getjob)
  * [Create a new job offer](#createjob)
  * [Get all applications for the job](#listapps)
  * [Get job application](#getapp)
  * [Create a new job application](#createapp)
  * [Change status of the application](#updateapp)
* [Error responses](#errors)
* [Testing](#test)

<a name="usage"></a>
## Usage

Clone repository with solution code, change to cloned dirrectory and start the service using *Maven*:
```
$ git clone https://github.com/aputivlskiy/java-interview-exercise.git

$ cd java-interview-exercise

$ mvn spring-boot:run
```

To access API you can either use *curl* as shown below or import and use supplied [Postman collection](./HeavenHR.postman_collection.json).

<a name="api"></a>
## API Endpoints
<a name="listjobs"></a>
### Get all job offers
Returns a list of existing job offers.

    GET /jobs

#### Request:
```json
curl --request GET --url http://localhost:8080/jobs
```
#### Response:
```json
Status: 200 OK
[
    {
        "id": 1,
        "jobTitle": "Junior software developer",
        "startDate": "01.01.2018",
        "numberOfApplications": 1
    },
    {
        "id": 2,
        "jobTitle": "Senior software developer",
        "startDate": "01.02.2018",
        "numberOfApplications": 0
    }
]
```
<a name="getjob"></a>
### Get job offer
Returns a job offer with specified Id

    GET /jobs/<offer_id>

#### Request:
```json
curl --request GET --url http://localhost:8080/jobs/1
```
#### Response:
```json
Status: 200 OK
{
    "id": 1,
    "jobTitle": "Junior software developer",
    "startDate": "01.01.2018",
    "numberOfApplications": 1
}
```
<a name="createjob"></a>
### Create a new job offer
Creates a new job offer with fields, specified within request body JSON object.

    POST /jobs

#### Request:
```json
curl --request POST \
  --url http://localhost:8080/jobs \
  --header 'Content-Type: application/json; charset=utf-8' \
  --data '{
	"jobTitle":"Software developer",
	"startDate":"25.02.2018"
  }'
```
#### Response:
URI of a newly created job offer is returned in the `Location` header with response.
  ```json
Status: 201 Created  
Location: http://localhost:8080/jobs/3
```
<a name="listapps"></a>
### Get all applications for the job
Returns a list of an applications for the specified job offer. I suppose use case, where this request is used to present a list of all application and thus, some fields are skipped for brevity: `resume` text and parent `jobOffer`.

    GET /jobs/1/applications

#### Request:
```json
curl --request GET --url http://localhost:8080/jobs/1/applications
```
#### Response:
```json
Status: 200 OK
[
    {
        "id": 1,
        "email": "my@email.com",
        "status": "APPLIED"
    },
    {
        "id": 2,
        "email": "some@email.com",
        "status": "APPLIED"
    }
]
```
<a name="getapp"></a>
### Get job application
Returns applications for the offer, specified by application Id.

    GET /jobs/<offer_id>/applications/<application_id>

#### Request:
```json
curl --request GET --url http://localhost:8080/jobs/1/applications/1
```
#### Response:
```json
Status: 200 OK
{
    "jobOffer": {
        "id": 1,
        "jobTitle": "Junior software developer",
        "startDate": "01.01.2018",
        "numberOfApplications": 2
    },
    "id": 1,
    "email": "some@email.com",
    "resume": "I'm a JavaScript developer with 20+ years of experience in React.js",
    "status": "APPLIED"
}
```
<a name="createapp"></a>
### Create a new job application
Creates a new job application for the specified job offer, with fields, specified within request body JSON object.

    POST /jobs/1/applications

#### Request:
```json
curl --request POST \
  --url http://localhost:8080/jobs/1/applications \
  --header 'Content-Type: application/json; charset=utf-8' \
  --data '{
	"email":"some@other.server.com",
	"resume":"Hello, here comes my resume..."
  }'
```
#### Response:
URI of a newly created job application is returned in the `Location` header with response.
  ```json
Status: 201 Created
Location: http://localhost:8080/jobs/1/applications/2
```
<a name="updateapp"></a>
### Change application status
Progresses the status of an application to a new specified status.

    PATCH /jobs/1/applications/1

#### Request:
```json
curl --request PATCH \
  --url http://localhost:8080/jobs/1/applications/1 \
  --header 'Content-Type: application/json; charset=utf-8' \
  --data '{
	"status":"INVITED"
  }'
```
#### Response:
URI of a newly created job application is returned in the `Location` header with response.
```
Status: 204 No Content
```
<a name="errors"></a>
## Error responses
In case of error, service returns JSON object, containing details of the error.
```JSON
{
    "timestamp": 1517676730038,
    "status": 400,
    "error": "Bad Request",
    "exception": "com.heavenhr.exercise.jobs.error.JobTitleNotUniqueException",
    "message": "Job offer with same title already exists: Software developer",
    "path": "/jobs"
}
```

### Error response codes
Besides general errors handling, provided by SpringBoot, service will return errors in the next cases:
<table>
  <tr>
    <th colspan="2">400 Bad Request</th>
  </tr>
  <tr>
    <td>
      <ul>
      <li>when job offer title is not unique</li>
      <li>when job offer create request body is missing required fields or field format is not valid</li>
      <li>when job application email is not unique within job offer</li>
      <li>when job application create request body is missing required fields or field format is not valid</li>
      </ul>
    </td>
  </tr>
  <tr>
    <th colspan="2">403 Forbidden</th>
  </tr>
  <tr>
    <td>
      <ul>
      <li>when trying to change job application status to the value, which is not supported by application workflow</li>
      </ul>
    </td>
  </tr>
  <tr>
    <th colspan="2">404 Not Found</th>
  </tr>
  <tr>
    <td>
      <ul>
      <li>when specified job offer or application not exists</li>
      </ul>
    </td>
  </tr>
</table>

<a name="test"></a>
## Testing
Solution includes **unit tests** for a two service classes: `JobOfferService` and `JobApplicationService`. Unit test for other classes are omitted.
There is also one **integration test** for Jobs API: `JobOfferApiTest`. For the sake of simplicity, integration tests are included in the same module as base source code.

To run all the tests execute next *Maven* command:

    $ mvn clean test

***
# Task

Build a backend service that handles a (very simple) recruiting process. The process requires two types of objects: job offers and applications from candidates.
minimum required fields for the objects are:

    Offer:
        jobTitle (unique)
        startDate
        numberOfApplications

    Application:
        related offer
        candidate email (unique per Offer)
        resume text
        applicationStatus (APPLIED, INVITED, REJECTED, HIRED)
Not all of the fields have to be persisted. You may use ad hoc calculation, event sourcing, or whatever you see fit. These are the fields that must be returned by the API. You may add fields where necessary.

## Use cases

- user has to be able to create a job offer and read a single and list all offers.
- candidate has to be able to apply for an offer.
- user has to be able to read one and list all applications per offer.
- user has to be able to progress the status of an application.
- user has to be able to track the number of applications.
- status change triggers a notification (*)

(*) a log output will suffice as a notification here, but you should design it as if each status change triggers a completely different business case.

## Technical requirements

use SpringBoot to build this service. The service must run standalone and must not require any third party software to be installed.
the service must communicate Json over http (REST).
return proper status codes for the most common problems.
the data does not have to be stored permanently, it may be handled in-memory during runtime.

## Things we are looking for

- a description how to build and use the service
- clean code
- use of the spring framework and spring best practices
- structure of the project
- how you test your code

We do not require you to build a frontend, only endpoints are relevant.
And we can't stress enough: we are looking for clean, structured code. If the task takes you too long, you can hard code a shortcut, mark it with a comment and it will be fine.
But make sure everything you hand in is well laid out and tidy!

Please send a link to a repository with your solution or a file archive to tech@heavenHR.com
