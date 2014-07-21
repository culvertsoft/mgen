require.config({
    baseUrl: EnvJasmine.rootDir,
    paths: {
        mocks: EnvJasmine.mocksDir,
        specs: EnvJasmine.specsDir,
        mGen: 'target/javascript/mgen-lib',
        mgen_classreg: 'src_generated/test/javascript/mgen-data'
    }
});