describe("Remote Control", function() {
	var fixtures;

    beforeEach(function(){
		fixtures = jasmine.getFixtures();
  		fixtures.fixturesPath = 'base/site/spec'
  		loadFixtures('switch-button-fixture.html')

  		spyOn(window, 'startRemoteControl');
	});

	it("should not start to remote control when checkbox is not clicked", function() {
		bindSwitchButton();

		expect(window.startRemoteControl).not.toHaveBeenCalled();
	});

	it("start to remote control when checkbox is clicked", function() {    
		bindSwitchButton();
		$(".main input[type='checkbox']").trigger('click');

		expect(window.startRemoteControl).toHaveBeenCalled();
	});

	it("should not start to remote control when checkbox is clicked 2 times", function() {
		bindSwitchButton();
		$(".main input[type='checkbox']").trigger('click');
		$(".main input[type='checkbox']").trigger('click');

		expect(window.startRemoteControl.callCount).toEqual(1);
	});

});

