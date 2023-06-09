# Web/MX&trade; Workshop

A home for the pedagogically-minded evolution of neat Web/MX apps.

Please check our nascent Wiki pages for more, including our inaugural project, a Web/MX inspector utility.

## Development

To get an interactive development environment run:
```bash
lein fig:build
```

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:
```bash
(js/alert "Am I connected?")
```
...and you should see an alert in the browser window.

To clean all compiled files:
```bash
lein clean
```
To create a production build run:
```bash
lein clean
lein fig:min
```

## License

Copyright © 2023 by Kenny Tilton

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
