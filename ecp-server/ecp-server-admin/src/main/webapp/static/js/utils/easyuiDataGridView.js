var myview = $.extend({}, $.fn.datagrid.defaults.view, {
	    renderFooter: function(target, container, frozen){
	        var opts = $.data(target, 'datagrid').options;
	        var rows = $.data(target, 'datagrid').footer || [];
	        var fields = $(target).datagrid('getColumnFields', frozen);
	        var table = ['<table class="datagrid-ftable" cellspacing="0" cellpadding="0" border="0"><tbody>'];
	         
	        for(var i=0; i<rows.length; i++){
	            var styleValue = opts.rowStyler ? opts.rowStyler.call(target, i, rows[i]) : '';
	            var style = styleValue ? 'style="' + styleValue + '"' : '';
	            table.push('<tr class="datagrid-row" datagrid-row-index="' + i + '"' + style + '>');
	            table.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
	            table.push('</tr>');
	        }
	         
	        table.push('</tbody></table>');
	        $(container).html(table.join(''));
	    }
	});