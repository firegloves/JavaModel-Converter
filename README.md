# JavaModel-Converter
An easy to use automation to generate transpiled code starting from Java Model objects

Java 8+ is required.

### Launch options

Put java classes to transpile into `[project_root]/resources/`, then launch the application.

To launch you can choose from these options:

- open the project in your IDE, add a Run Configuration and launch it
- launch jar included into dist folder, optionally passing parameters

You can make some configurations into application.properties file. It is already setted with a working configuration.
By default you need to put your to transpile java classes into `[project_root]/transpiling` folder.


**WHEN YOU COPY PASTE YOUR JAVA CLASSES TO TRANSPILE INTO SOME IDE CHECK IF IT HAS REMOVED JAVA PACKAGE. WITHOUT PACKAGE JavaModel-Converter CAN'T WORK (for now ;) )**

### Supported languages

At this moment only Java to Typescript transpiling is supported.

*If you want to develop other language transpiling processes, feel free to contribute*


**Typescript**

Transpiling process supports code generation of classes with class members and constructor.

Interface code generation is not still supported.

Supported Java data types:

- int
- java.lang.Integer
- float
- java.lang.Float
- long
- java.lang.Long
- java.math.BigDecimal

transpiled to *Typescript number*

----------------------

- java.lang.String

transpiled to *Typescript string*

----------------------

- java.util.Date
- java.sql.Date
- java.sql.Timestamp

transpiled to *Typescript Date*

----------------------

- boolean
- java.lang.Boolean

transpiled to *Typescript boolean*

----------------------

- arrays
- java.util.Collection, its subinterfaces and implementing types. Raw and parametrized types
- java.util.Map, its subinterfaces and implementing types. Raw and parametrized types

transpiled to *Typescript Array*

---------------------

Custom data types included into current transpiling process are now supported
