# Process KJAR Archetype

Archetype used to build new Red Hat Process Automation Manager project for processes which can be imported into Business Central.

## Generate KJAR project

1. Build the process-kjar-archetype module (mvn clean install)
2. Change to directory of your choice where you want to build the kjar from this archetype.
3. Create your new base kjar project from the archetype with:

```
mvn archetype:generate
   -DarchetypeGroupId=com.redhat.pam
   -DarchetypeArtifactId=process-kjar-archetype
   -DarchetypeVersion=7.7
   -DgroupId=com.company
   -DartifactId=test-kjar
   -Dversion=1.0-SNAPSHOT
   -Dpackage=com.company
```

or use this one-liner

```
mvn archetype:generate -DarchetypeGroupId=com.redhat.pam -DarchetypeArtifactId=process-kjar-archetype -DarchetypeVersion=7.7 -DgroupId=com.company -DartifactId=test-kjar -Dversion=1.0-SNAPSHOT -Dpackage=com.company
```

4. Change the prompted values during the generation as needed (or leave the defaults)
5. Compile and test your generated base kjar project with

```
mvn clean install
```

## Import project to KIE Workbench

After generating your project you can import it into Business Central.
In your project directory:

1. Initialize git for your project

```
git init
```

2. Add all your project files to git

```
git add .
```

3. Commit all your project files to git

```
git commit -m "YOUR COMMIT MESSAGE HERE"
```

In your KIE Workbech Instance

1. In the Space View select "Import Project"
2. Type the path to your git kjar project, for example

```
file://myuser/mypath/to/my/new/project/projectname
```

Business Central should now import your created kjar project and you can start working on adding new assets to it etc.

## Troubleshooting

This archetype requires maven-archetype-plugin version 3.0.1 or above.
In case you run into issues with the post generation scripts not being executed during archetype generation run it once with force update maven option

```
-U
```

This will make sure you up the 3.0.1 version of maven-archetype-plugin and get it installed in your local maven repo.
