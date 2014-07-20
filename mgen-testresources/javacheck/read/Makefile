generate: get-generated-sources
	sbt test
	
get-generated-sources:
	mkdir -p src_generated
	mkdir -p src_generated/test
	cp -rf ../../generated/read/src_generated/java src_generated/test/

clean: 	
	sbt clean
	rm -rf src_generated
	rm -rf target/*
