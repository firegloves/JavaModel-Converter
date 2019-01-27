# JavaModel-Converter

An easy to use automation to generate transpiled code starting from Java Model objects

Java 8+ is required.

### Launch

Open the project in your IDE, add a Run Configuration and launch it

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


### Contribute

To develop new language conversion follow this step:

1. add new language to `ESupportedLanguages` enum, for example for Swift add something like `SWIFT`
2. add a `swift` package into `it.caneserpente.javamodelconverter.converter`
3. into `it.caneserpente.javamodelconverter.converter.swift` package add 4 classes to extends all `it.caneserpente.javamodelconverter.converter.base` classes
4. implement previously created classes abstract methods
5. into `it.caneserpente.javamodelconverter.builder.ClassConverterDirector` add new language method, for example `cosntructSwift()` (look at `constructTypescript()` for an example)
6. into `it.caneserpente.javamodelconverter.builder.ClassConverterDirector.configureMap()` method add new lang mapping, for example `this.map.put(ESupportedLanguages.SWIFT, () -> this.constructSwift());`
7. finally into `application.properties` change `target.language` value to `SWIFT` and let's transpile!
