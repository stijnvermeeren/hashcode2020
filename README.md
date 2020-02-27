# Hash Code 2020 solution

This is a clean-up version of the [Scala](https://www.scala-lang.org/) solution of the team _Starmind Scala_ to the [Hash Code](https://codingcompetitions.withgoogle.com/hashcode) 2020 challenge from Google.

## Challenge

The details of the challenge, including the datasets, can be found in the [challenge](./challenge) folder.

## Result

This solution achieved 26,929,308 points, putting us at position 194 in the [ranking](https://codingcompetitions.withgoogle.com/hashcode/archive/2020), with more than 10,000 teams participating.

## Team

The team consisted of [Starmind](https://www.starmind.ai/) employees

- Stijn Vermeeren ([stijnvermeeren](https://github.com/stijnvermeeren/))
- Nicu Reut ([ecyshor](https://github.com/ecyshor/))
- Felix Kieber ([Foesec](https://github.com/Foesec/))

with additional support from

- Stefan Jacobs ([stefanjacobs123](https://github.com/stefanjacobs123/))

## Method

Our solution is essentially a greedy search: whenever we can sign up another library, pick the one with the highest _score_, considering the books that will already be scanned anyway by previously selected libraries.

The _score_ of a library is composed of...

- The maximum value that we can still get from the library (i.e. by signing it up immediately and scanning the books that are not already scanned from other libraries from highest value to lowest value).
- The number of days it takes to sign up this library (we essentially pick a library with maximal value per day of sign-up time).

## Running

The Scala code can be compiled and executed using [sbt](https://www.scala-sbt.org/).

```
sbt "runMain challenge.Main"
```

The output will be written to the `output` directory.
