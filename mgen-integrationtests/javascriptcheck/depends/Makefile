run-tests: get-generated-sources
	sbt jasmine

get-javascript:
	mkdir -p src/main
	mkdir -p src/main/javascript
	cp -f ../../../mgen-javascriptlib/target/javascript/* src/main/javascript/

get-test-data:
	mkdir -p src_generated
	mkdir -p src_generated/test
	cp -f ../../generated/depends/src_generated/javascript/* src_generated/test/

get-generated-sources: get-javascript get-test-data

clean: 	
	sbt clean
	rm -rf src/main
	rm -rf src_generated
	rm -rf target/*