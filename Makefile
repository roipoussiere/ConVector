MAKEFLAGS += --no-builtin-rules --silent --keep-going --jobs 12

convector.jar: $(shell find src -name '*.java')
	mkdir -p tmp
	pkill -xf 'java -ea -jar convector.jar 3434' || true
	LANG=en_US.UTF-8 javac -encoding UTF-8 -classpath bin -sourcepath src -source 1.8 -d tmp $?
	cd tmp; zip -q -r -m ../$@ *; cd ../src; zip -q ../$@ `find . -type f -not -name '*.java'`
	rmdir tmp

serve:
	while true; do java -ea -jar convector.jar 3434; done

test: test/*.svg test/*.ps
examples: examples/*.svg examples/*.ps

%.svg %.ps: convector.jar
	scripts/montage.sh $@
	touch $@

doc:
	doxygen doc/Doxyfile

sonar: convector.jar
	sonar-runner
	DISPLAY=:1 xdg-open .sonar/issues-report/issues-report-light.html

.PHONY: serve test examples doc sonar
