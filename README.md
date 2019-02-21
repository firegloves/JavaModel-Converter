# JavaModel-Converter

An easy to use automation to generate transpiled code starting from Java Model objects

Java 8+ is required.

### Launch

**Command line**

You can launch `dist/JavaModel-Converter.jar` from command line.
You can choose from there parameters:

`-l`, `--lang`: Target transpiling languages

`-s`, `--src`: Java model classes folder path

`-c`, `--compiled`: Java compiled classes folder path

`-g`, `--generated`: Transpiled model classes folder path

Example command: 
`java -jar JavaModel-Converter.jar -l typescript -s /media/gandalfDrive -c /media/gandalfDrive/compiled -g /media/gandalfDrive/generated`

**IDE**

Open the project in your IDE and launch it

You can make some configurations into application.properties file. It is already setted with a working configuration.

By default you need to put your to transpile java classes into `[project_root]/transpiling` folder.

**WHEN YOU COPY PASTE YOUR JAVA CLASSES TO TRANSPILE INTO SOME IDE CHECK IF IT HAS REMOVED JAVA PACKAGE. WITHOUT PACKAGE JavaModel-Converter CAN'T WORK (for now ;) )**

### Supported languages

At this moment only Java to Typescript transpiling is supported.

*If you want to develop other language transpiling processes, feel free to contribute*


**Typescript**

Transpiling process supports code generation of classes with class members and constructor.

Interface, enum and class code generation is supported. You can configure what to generate into `application.properties`'s `typescript.generateInterface`
Keep in mind how [interfaces work in Typescript](https://www.typescriptlang.org/docs/handbook/interfaces.html).
I suggest to use almost always classes to take full advantage of type power. This means that to take advantage of Typescript typization when you have a json object (for example in response to an http request) you should create a new instance.

For example in Angular:

`// with a single Foo
httpService.getFoo().subscribe(fooResponse => {
    this.foo = new Foo(fooResponse);                            // fooResponse is a JSON object matching Foo class properties
});

// with an Foo array 
httpService.getFoo().subscribe(fooArrayResponse => {
    this.fooArray = fooArrayResponse.map(f => new Foo(f));      // fooArrayResponse is a JSON array of object matching Foo class properties
});`

*Angular coding style is now supported*

You can set it by setting `application.properties`'s `typescript.angularCodingStyle` to true.
Default file name example: PojoTest.ts
Angular file name example: pojo-test.model.ts

*Prefix and Suffix*

You can add a prefix and / or a suffix to append to generated filenames.
You can configure them into `application.properties`'s `filename.prefix` and `application.properties`'s `filename.suffix`
Actually Angular coding style and suffix are conflicting, don't mix them please

*Supported Java data types*


| Java                      |      Typescript       | 
|---------------------------|-----------------------|
| int                       |                       |
| java.lang.Integer         |                       |
| float                     |                       |
| java.lang.Float           |                       |
| long                      |                       |
| java.lang.Long            |                       |
| java.math.BigDecimal      |       number          |
| java.math.BigInteger      |                       |
| double                    |                       |
| java.lang.Double          |                       |
| byte                      |                       |
| java.lang.Byte            |                       |
| short                     |                       |
| java.lang.Short           |                       |
|---------------------------|-----------------------|
| char                      |                       |
| java.lang.Character       |       string          |
| java.lang.String          |                       |
|---------------------------|-----------------------|
| java.util.Date            |                       |
| java.sql.Date             |       Date            |
| java.sql.Timestamp        |                       |
|---------------------------|-----------------------|
| boolean                   |                       |
| java.lang.Boolean         |       boolean         |
|---------------------------|-----------------------|
| arrays                    |                       |
| java.util.Collection**    |       Array           |
| java.util.Map***          |                       |
|---------------------------|-----------------------|
| Custom data types****     |  Custom data types    |
|---------------------------|-----------------------|

** java.util.Map, its subinterfaces and implementing types. Raw and parametrized types

*** java.util.Collection, its subinterfaces and implementing types. Raw and parametrized types 

**** Custom data types included into current transpiling process are now supported

---------------------


### Contribute

To develop new language conversion follow this step:

1. add new language to `ESupportedLanguages` enum, for example for Swift add something like `SWIFT`
2. add a `swift` package into `it.caneserpente.javamodelconverter.converter`
3. into `it.caneserpente.javamodelconverter.converter.swift` package add 4 classes to extends all `it.caneserpente.javamodelconverter.converter.base` classes
4. implement previously created classes abstract methods
5. into `it.caneserpente.javamodelconverter.builder.ClassConverterDirector` add new language method, for example `cosntructSwift()` (look at `constructTypescript()` for an example)
6. into `it.caneserpente.javamodelconverter.builder.ClassConverterDirector.configureMap()` method add new lang mapping, for example `this.map.put(ESupportedLanguages.SWIFT, () -> this.constructSwift());`
7. finally into `application.properties` change `target.language` value to `SWIFT` and let's transpile!
