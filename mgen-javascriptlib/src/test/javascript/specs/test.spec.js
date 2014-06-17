describe("A suite is just a function", function() {
  var a;

  it("and so is a spec", function() {
    a = true;

    expect(a).toBe(true);
  });
});


describe("The 'toBe' matcher compares with ===", function() {

  it("and has a positive case", function() {
    expect(false).toBe(true);
  });

  it("and can have a negative case", function() {
    expect(false).not.toBe(true);
  });
});

