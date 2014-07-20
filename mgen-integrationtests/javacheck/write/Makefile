run-tests: get-generated-sources
	sbt test
	
get-generated-sources:
	mkdir -p src_generated
	mkdir -p src_generated/test
	cp -rf ../../generated/write/src_generated/java src_generated/test/

clean: 	
	sbt clean
	rm -rf src_generated
	rm -rf target/*
