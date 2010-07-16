function createGrid(dt, col, colProps, id, title, height) {
	var myReader = new Ext.data.ArrayReader({}, col);

	var grid = new Ext.grid.GridPanel({
		store: new Ext.data.Store({
			data: dt,
			reader: myReader
		}),

		columns:colProps,


		renderTo: id,
		title: title,
		height: height,
		frame: true
	});

	grid.getSelectionModel().selectFirstRow();

}




function createGlobalTabs(idsAndTitles) {
    var tabs = new Ext.TabPanel({
        renderTo: 'globalTabs_shell',
        activeTab: 0,
        frame:true,
		//width:1000,
		//height: 700,
        //defaults:{autoScroll: true},
        items:idsAndTitles
    });
}