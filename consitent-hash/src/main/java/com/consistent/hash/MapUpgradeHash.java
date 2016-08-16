package com.consistent.hash;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapUpgradeHash {
	private NodeMap nodeMap;

	public MapUpgradeHash(NodeMap nodeMap){
		this.nodeMap = nodeMap;
		init();
	}
	
	/**
	 * 初始化主节点和虚拟节点
	 */
	public void init(){
		TreeMap<Long,String> nodes = new TreeMap<Long, String>();
		for(Map.Entry<String, BigDecimal> entry:nodeMap.getShards().entrySet()){
			for(int n=0;n<nodeMap.getNODE_NUM();n++){
				nodes.put(hash(entry.getKey()+n), entry.getKey());
			}
		}
		nodeMap.setNodes(nodes);
	}
	
	/**
	 * 获得主节点
	 * @param key
	 * @return
	 */
	public synchronized String getShardInfo(String key) {
		SortedMap<Long, String> tail = nodeMap.getNodes().tailMap(hash(key)); // 沿环的顺时针找到一个虚拟节点
		if (tail.size() == 0) {
			if(nodeMap.getNodes().size()>0){
				return nodeMap.getNodes().get(nodeMap.getNodes().firstKey());
			}else{
				return null;
			}
		}
		return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实机器节点的信息
	}
	
	/**
	 * 获取第一个节点，并且删除
	 * @return
	 */
	public synchronized Account getFirstBackup(){
		if(nodeMap.getBackup().size()>0){
			Account account = null;
			for(Map.Entry<String, BigDecimal> entry:nodeMap.getBackup().entrySet())
			account = new Account(entry.getKey(),entry.getValue());
			nodeMap.getBackup().remove(account.getAccountId());
			return account;
		}
		return null;
	}
	/**
	 * 添加主账户
	 * @param account
	 */
	public synchronized void addShare(Account account ){
		this.nodeMap.getShards().put(account.getAccountId(), account.getAmt());
		for(int n=0;n<nodeMap.getNODE_NUM();n++){
			nodeMap.getNodes().put(hash(account.getAccountId()+n), account.getAccountId());
		}
	}
	
	/**
	 * 删除主账户
	 * @param account
	 */
	public synchronized void removeShare(Account account){
		if(this.nodeMap.getShards().size()>0){
			this.nodeMap.getShards().remove(account.getAccountId());
			for(int n=0;n<nodeMap.getNODE_NUM();n++){
				nodeMap.getNodes().remove(hash(account.getAccountId()+n));
			}
		}
	}
	
	/**
	 * 添加备用账户
	 * @param account
	 */
	public synchronized void addBackup(Account account){
		this.nodeMap.getBackup().put(account.getAccountId(), account.getAmt());
	}
	
	/**
	 * 删除备用账户
	 * @param account
	 */
	public synchronized void removeBackup(Account account){
		if(this.nodeMap.getBackup().size()>0)
			this.nodeMap.getBackup().remove(account.getAccountId());
	}
	
	public NodeMap getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(NodeMap nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	/**
	 *  MurMurHash算法，是非加密HASH算法，性能很高，
	 *  比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
	 *  等HASH算法要快很多，而且据说这个算法的碰撞率很低.
	 *  http://murmurhash.googlepages.com/
	 */
	private Long hash(String key) {
		
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;
		
		ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
	}
}
