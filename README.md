# Boardgames

A companion PWA for *7 Wonders*  and *Orleans Stories* built using Scala.js.

## Setup

### Install node dependencies:

```
cd app && npm install
```

Alternatively, you can run it directly from sbt, from the root repository:

```
sbt npmInstall
```

This is provided as a convenience by the `NextApp` plugin. 
However, running external commands from sbt appears to
be significantly slower than running them from a terminal, at least on my laptop :(

## Development

### Launch the Next.js dev server

```
cd app && npm run dev
```

Again, if you'd like to run everything from sbt, the `NextApp` plugin provides several sbt commands:
- `startNextServer` to start the dev server in the background. Does nothing if the server is already running.
- `stopNextServer` to stop the dev server
- `show nextServerIsRunning` to check if the dev server is running


### Build the Javascript from Scala.js (and watch for changes):

In another terminal:

```
sbt ~fastLinkJS
```

## Production build

### 1) Build the Javascript from Scala.js (with full optimization):

```
sbt fullLinkJS
```

### 2) Build the Next.js app

```
cd app && npm run build
```

Or, from sbt:

```
sbt nextBuild
```

