run-tests: get-generated-sources
	sbt jasmine

get-test-data:
	mkdir -p src_generated
	mkdir -p src_generated/test
	mkdir -p src_generated/test/javascript
	cp -f ../../generated/depends/src_generated/javascript/* src_generated/test/javascript/

get-generated-sources: get-test-data

clean: 	
	sbt clean
	rm -rf src_generated
	rm -rf target/*