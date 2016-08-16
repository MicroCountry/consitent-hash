package com.consistent.hash;

import java.util.List;
import java.util.TreeMap;

public class NodeList<S> {
	private TreeMap<Long, S> nodes; // 虚拟节点
	private List<S> shards; // 真实机器节点
	private List<S> backup;
	private final int NODE_NUM = 100; // 每个机器节点关联的虚拟节点个数
	
	public NodeList(List<S> shards,List<S> backup){
		this.shards = shards;
		this.backup = backup;
	}
	
	public int getNODE_NUM() {
		return NODE_NUM;
	}
	public TreeMap<Long, S> getNodes() {
		return nodes;
	}
	public void setNodes(TreeMap<Long, S> nodes) {
		this.nodes = nodes;
	}
	public List<S> getShards() {
		return shards;
	}
	public void setShards(List<S> shards) {
		this.shards = shards;
	}
	public List<S> getBackup() {
		return backup;
	}
	public void setBackup(List<S> backup) {
		this.backup = backup;
	}
	
}
