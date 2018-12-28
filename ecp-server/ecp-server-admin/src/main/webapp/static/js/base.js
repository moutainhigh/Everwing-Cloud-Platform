function formatDateTime(date){
	 return date ? moment(new Date(date)).format('YYYY-MM-DD HH:mm:ss') : ""; 
}
function formatDate(date){
	 return date ? moment(new Date(date)).format('YYYY-MM-DD') : ""; 
}