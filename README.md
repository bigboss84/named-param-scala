# named-param-scala

# Table Of Contents
1. [Overview](#overview)
1. [Supported types](#supported-types)
    - [Numeric and Boolean values](#numeric-and-boolean-values)
    - [Strings and objects](#strings-and-objects)
    - [Option](#option)
1. [Translation from map](#translation-from-map)
1. [Translation from model](#translation-from-model)
1. [Custom converters](#custom-converters)

## Overview
Simple helper to make statements from a named parameters template strings.

Suppose that we have the following parametrized SQL statement:

```scala
val sql = "SELECT id FROM tree WHERE name = :name"
```

we want to obtain a runnable statement like following:

```
SELECT id FROM tree WHERE name = 'oak'
```

this library helps us to translate named parameters with their values,
it supports also `scala.Option` and custom converters.

## Supported types
Library supports conversion for basic scala numeric and boolean
values, characters, strings, `scala.Any` and `scala.Option` types.

## Numeric and Boolean values
`Byte`, `Short`, `Int`, `Long`, `Float`, `Double` and `Boolean` values will simply
translated with their values.

For example following statement
```
SELECT
  name
FROM
  garden
WHERE
  num_of_trees = :numberOfTrees
  AND has_evergreen = :hasEvergreen
```

and for parameters: `"numberOfTrees" -> 3` and `"hasEvergreen" -> true`
the statement will be translated as follows:

```
SELECT
  name
FROM
  garden
WHERE
  num_of_trees = 3
  AND has_evergreen = true
```

## Strings and objects
`Char`, `String` and `Any` objects will be single-quoted calling `toString` method.

For example following statement

```
SELECT
  *
FROM
  tree
WHERE
  name = :name
  AND descr = :descr
  AND `type` = :treeType
```

and for following class:

```scala
case class TreeDescription() {
  override def toString: String = "wonderful plant"
}
```

and for parameters: `"name" -> "evergreen"`, `"descr" -> TreeDescription()` and `"treeType" -> 'e'`
the statement will be translated as follows:

```
SELECT
  *
FROM
  tree
WHERE
  name = 'evergreen'
  AND descr = 'wonderful plant'
  AND `type` = 'e'
```

## Option
The library supports type `Option[T]` then it automatically and recursively
handles the type of boxed object and `None`.

## Translation from map
Translation can be performed by an input `Map` using `<<` infix operator
like the following example:

```scala
import it.russoft.namedparam.Implicits._
"SELECT * FROM tree WHERE name = :name" << Map("name" -> "evergreen") 
```

## Translation from model
Translation can also be performed by an instance of a case class using `<<<`
infix operator, for example:

```scala
case class Tree(name: String)

import it.russoft.namedparam.Implicits._
"SELECT * FROM tree WHERE name = :name" <<< Tree("evergreen") 
```

## Custom converters
Library supports custom converters that allows to override default case class
instance conversion, to obtain this behaviour it is very simple declaring
your custom converter as follows:

```scala
//
// [other imports]
//
import it.russoft.namedparam.Implicits._

implicit val converters: Converter =
    classOf[Date] -> new NamedParamConverter[Date](x => new SimpleDateFormat("yyyy-MM-dd").format(x)) :: Nil

"SELECT * FROM tree WHERE registeredAt = :registeredAt" << Map("registeredAt", new Date())
```