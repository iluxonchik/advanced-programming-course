# Ant

Notes from [Ant's begginer's guide](https://ant.apache.org/manual/using.html)

* **Target** - set of tasks. A target may depend on other targets (ex: target
  for creating a distributable might depend on the *compile* target)
* **Task** - piece of code that can be executed. Task can have multiple
attributes (A.K.A. arguments).
* **Properties** - used to customize the build process or provide shortcuts
for strings that are used repeatedly inside a build file. A property has a name
and a value. For example, if you have defines `<property name="some_dir" location="build" />`,
`${build}/compile` will be resolved at runtime to `some_dir/compile`.
