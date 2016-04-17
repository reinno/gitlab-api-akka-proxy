# Gitlab Api Akka Enhanced
Use Akka to enhance Gitlab Api

# Status
[![Build Status](https://travis-ci.org/reinno/gitlab-api-akka-proxy.svg)](https://travis-ci.org/reinno/gitlab-api-akka-proxy)

# How to build
```
git clone git@github.com:reinno/gitlab-api-akka-proxy.git
cd gitlab-api-akka-proxy
sbt clean assembly
```

# Extern API List
## Notes
### Issues
* Get project issue comments number
Get number of notes of issues of a project 
```
GET /projects
/:id/issues/notes_num 
```
Parameters:  
    * `id`(required) - The ID of a project
```
{
  "notes_num": 10
}
```