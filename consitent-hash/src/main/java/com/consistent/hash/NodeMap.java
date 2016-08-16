package com.consistent.hash;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class NodeMap {
	private TreeMap<Long, String> nodes = new TreeMap<Long, String>(); // 虚拟账户<hash值,账户号>
	private ConcurrentHashMap<String,BigDecimal> shards = new ConcurrentHashMap<String, BigDecimal>(); // 真实账户 <账户号,余额>
	private ConcurrentHashMap<String,BigDecimal> backup = new ConcurrentHashMap<String, BigDecimal>(); //备用账户 <账户号,余额>
	private final int NODE_NUM = 100; // 每个真实节点关联的虚拟节点个数
	public TreeMap<Long, String> getNodes() {
		return nodes;
	}
	public void setNodes(TreeMap<Long, String> nodes) {
		this.nodes = nodes;
	}
	public ConcurrentHashMap<String, BigDecimal> getShards() {
		return shards;
	}
	public void setShards(ConcurrentHashMap<String, BigDecimal> shards) {
		this.shards = shards;
	}
	public ConcurrentHashMap<String, BigDecimal> getBackup() {
		return backup;
	}
	public void setBackup(ConcurrentHashMap<String, BigDecimal> backup) {
		this.backup = backup;
	}
	public int getNODE_NUM() {
		return NODE_NUM;
	}
	
	
}
