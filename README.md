# imbox-client-network

## DESCRIPTION:

The *imbox-client-network* is a component of [imbox-client](https://github.com/jaiyalas/imbox-client). This package requires [imbox-infra](https://github.com/jaiyalas/imbox-infra) to compile.

## REQUIREMENTS:

* Java 8 (tested with Oracle Java 1.8.0_20-ea) 
* Maven 3 (tested with Apache Maven 3.2.1)

## INSTALLATION:

    > mvn clean
    > mvn compile
    > mvn package
    
or, just in single line

    > mvn clean compile package

### Integrate with your POM file

**Deploy jar as a sub-repository**

Create a directory to place imbox-infra.jar

    > cd /where/your/repo/is/
    > mkdir -p libs/org/imbox/imbox-client-network/<version>/

then copy imbox-client-network-<version>.jar into above directory.

**Add repository into your POM file**

Open your pom.xml file and add the following code:

	 <repositories>
	    <repository>
	      <id>libs-local</id>
	      <name>libs</name>
	      <url>file://${project.basedir}/libs</url>
	    </repository>
	  </repositories>
	  
now all of those folders under libs/ can be loaded via Maven.

**Add dependency for imbox-infra**

Open your pom.xml file and add the following code:

    <dependencies>
        <dependency>
            <groupId>org.imbox</groupId>
            <artifactId>imbox-client-network</artifactId>
            <version>*version-number*</version>
        </dependency>
    </dependencies>

where \*version-number\* is current version number, for example, 0.2.1.

## LICENSE:

Apache License 2.0

Copyright (c) 2014, Yih-Der Lee

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.    