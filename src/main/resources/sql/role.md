
sample
===
* 注释

	select #{use("cols")} from role  where  #{use("condition")}

cols
===
	id,role_name

updateSample
===
	
	id=#{id},role_name=#{roleName}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(roleName)){
	 and role_name=#{roleName}
	-- @}
	
	