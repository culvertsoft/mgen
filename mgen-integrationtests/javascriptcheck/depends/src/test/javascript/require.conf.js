require.config({
    baseUrl: EnvJasmine.rootDir,
    paths: {
        mocks: EnvJasmine.mocksDir,
        specs: EnvJasmine.specsDir,
        mGen: '../../../mgen-javascriptlib/src/main/javascript/mgen-lib',
        se_culvertsoft: 'src_generated/test/javascript/mgen-data'
    }
});