package com.zp.Jpa.service.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zp.Jpa.entity.Equity;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.entity.equity.EquityUser;
import com.zp.Jpa.entity.search.EquitySearch;
import com.zp.Jpa.repository.EquityRepository;
import com.zp.Jpa.repository.EquityUserRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.EquityService;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;

@Service
public class EquityServiceImpl implements EquityService {
	@Autowired
	private EquityRepository equityRepository;

	@Autowired
	private EquityUserRepository equityUserRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Override
	// 事务
	@Transactional(rollbackFor = Exception.class)
	// 基于类的代理模式
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public synchronized Map<String, Object> addEquity(Equity equity) {
		Map<String, Object> map = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		// 判断属于哪个阶段 0
		// 600-50k 10% 1
		// 50k-100k 15% 2
		// 100k-150k 20% 3
		// 150k-- 25% 4
		final BigDecimal directPush;
		BigDecimal level;

		try {
			if (!StringUtils.isEmpty(equity)) {
				// 判断子类和父类关系
				Integer uid = equity.getUId();
				List<Equity> equities = equityRepository.findByUId(uid);

				if (equities.size() > 0) {
					// 如果已经被人推荐过了
					Integer reId = equities.get(0).getRecommendId();
					if (!reId.equals(0) && !reId.equals(equity.getRecommendId())) {// 如果不是元老,recommendId为0的为元老级别,如果不是原来的推荐人id
						// List<Integer> recommendIds=new ArrayList<Integer>();//推荐人id
						// for(Equity e:equities) {
						// recommendIds.add(e.getRecommendId());
						// }
						// if(StringUtils.isEmpty(recommendIds.get(0))) {
						// if(recommendIds.get(0)!=reId) {//如果新添加的这条和原来的推荐人不符合
						// System.out.println("该用户已经被推荐过了,不可重复推荐,推荐人id==>"+reId);
						// return null;
						// }
						// }
						System.err.println("!!!!该用户已经被推荐过了,不可重复推荐,原有的推荐人id==>" + reId);
						return null;
					}
					if (equity.getUId().equals(equity.getRecommendId())) {
						System.err.println("!!!!不可以自己推荐自己!!!==>" + equity.getUId());
						return null;
					}
					// 根据推荐人id 当做 用户id去查询recommend,看看是否等于 uid 来判定是不是互推
					Integer uidFalse = equity.getRecommendId();// 6032
					List<Equity> equities2 = equityRepository.findByUId(uidFalse);
					if (equities2.size() > 0) {
						Integer reId2 = equities2.get(0).getRecommendId();// 72
						if (!reId2.equals(0) && !StringUtils.isEmpty(equity.getUId())) {// 如果不是元老,recommendId为0的为元老级别
							if (reId2.equals(equity.getUId())) {// 互推行为
								System.err
										.println("不可以相互推荐,出现互推的uid为" + reId2 + "(父id)和(子id)" + equity.getRecommendId());
								return null;
							}

						}
					}
				}
				// 不可越级推荐,
				// 如果股东表中没有这个股东,就新增一个股东
				EquityUser equityUser2 = equityUserRepository.findByUId(uid);
				BigDecimal money = equity.getMoney();// 本次入股金额
				if (StringUtils.isEmpty(equityUser2)) {
					EquityUser equityUser = new EquityUser();
					equityUser.setUId(uid);
					// 保存新股东的信息,
					Users user = usersRepository.findByUid(uid);
					// 保存
					equityUser.setName(user.getTrueName());// 名字
					equityUser.setUrl(user.getPic());// 头像

					equityUser.setRecommendId(equity.getRecommendId());
					equityUser.setMoney(equity.getMoney());
					equityUser.setCreateTime(new Date());// 创建时间
					equityUser.setUpdateTime(new Date());// 修改时间
					equityUser.setIsDel(0);// 保存状态
					// 股东登记判断
					level = getLevel(equity.getMoney());
					equityUser.setLevel(level);
					// 添加一个股东
					equityUserRepository.save(equityUser);
				} else {

					// 如果是用户第二次入股,更新用户的登记
					if (!StringUtils.isEmpty(equityUser2.getMoney())) {
						BigDecimal totalMoney = equityUser2.getMoney().add(money);// 入股的总金额
						BigDecimal level2 = getLevel(totalMoney);
						equityUser2.setLevel(level2);
						equityUserRepository.save(equityUser2);
					}

				}

				directPush = getDirectPush(money);// 本次佣金比例
				//changeFatherDirectpush(uid, money);// 更改自己的先祖辈,更改所属团队的佣金比例

				BigDecimal commission = money.multiply(directPush);// 本次入股金额所产生的佣金
				BigDecimal bigDecimal = new BigDecimal("0.02");

				BigDecimal morePush = money.multiply(bigDecimal);// 本次入股金额所产生的越推佣金

				// 如果小于15万,就只保存父亲和爷爷
				if (directPush.compareTo(new BigDecimal("0.25")) == -1) { // 小于0.25

					// 尝试保存父亲的直推佣金
					Integer recommendId = equity.getRecommendId();
					if (!StringUtils.isEmpty(recommendId) && !recommendId.equals(0)) {
						EquityUser equity2 = saveCommission(recommendId, commission);
						if (!StringUtils.isEmpty(equity2)) {
							// 尝试保存爷爷的越推佣金
							Integer recommendId2 = equity2.getRecommendId();// 爷爷的用户id
							if (!StringUtils.isEmpty(recommendId2) && !recommendId2.equals(0)) {
								saveMore(recommendId2, morePush);
							}
						}

					}

				} else {// 保存一个父亲和往上数4个爷爷
					// 判断祖先层是否为空 ,算上爷爷层,再往上的祖先都可得2%;
					// 如果如果金额大于15万
					Integer recommendId = equity.getRecommendId();// 父亲的id
					if (!StringUtils.isEmpty(recommendId) && !recommendId.equals(0)) {
						// 尝试保存父亲的直推佣金
						EquityUser equity3 = saveCommission(recommendId, commission);
						// 尝试保存4个爷爷的越推佣金
						Integer recommendId3 = equity3.getRecommendId();// 爷爷的id
						if (!StringUtils.isEmpty(recommendId3) && !recommendId3.equals(0)) {
							List<EquityUser> equitiesGrandFather = new ArrayList<>();

							// 递归设置爷爷集合
							this.setEqutites(equitiesGrandFather, recommendId3);
							for (EquityUser equity2 : equitiesGrandFather) {
								if (!StringUtils.isEmpty(equity2.getMorePush())) {
									equity2.setMorePush(equity2.getMorePush().add(morePush));
								} else {
									equity2.setMorePush(morePush);
								}
								equity2.setIsDel(0);
								equityUserRepository.save(equity2);
							}
						}

					}

				}
				// 刷新自己的入股金额
				// 如果重复入股

				EquityUser equityUser = equityUserRepository.findByUId(uid);
				if (!StringUtils.isEmpty(equityUser)) {
					equityUser.setMoney(equityUser.getMoney().add(equity.getMoney()));
				} else {
					equityUser.setMoney(money);
				}
				equityUserRepository.save(equityUser);

				// 保存父类和爷爷类,也就是所属团队的佣金比例
				saveTeam(equity);

				equity.setDirectPush(directPush);// 直推百分比
				equity.setCreateTime(new Date());// 创建时间
				equity.setUpdateTime(new Date());// 修改时间
				equity.setIsDel(0);// 保存状态
			}
			map.put("data", equityRepository.save(equity));
			map.put("msg", "入股成功");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("在执行本次入股事务中出现意外异常,数据已回滚");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 如果updata2()抛了异常,updata()会回滚,不影响事物正常执行
			map.put("msg", "本次入股数据出现异常,请联系官方微信,或稍后再试");
		}
		return map;
	}

	// 所属团队的佣金比例
	private void saveTeam(Equity equity) {
		System.out.println("所属团队的佣金比例");
		// TODO Auto-generated method stub
		List<Integer> uids = new ArrayList<>();// 更改总代数
		Integer fatherId = 0;
		Integer uid = equity.getUId();
		for (int i = 0; i < ConstantUtil.TEAMEQUITY; i++) {
			if (!StringUtils.isEmpty(uid) && !uid.equals(0)) {
				uids.add(uid);
				// 如果父亲id合格,就放入总代数中
				if (!StringUtils.isEmpty(fatherId) && !fatherId.equals(0)) {
					uids.add(fatherId);
					// 每次循环都要更新uid和他的父亲id
					uid = getFatherId(uid);
				}
				fatherId = getFatherId(uid);
			}

		}
		for (Integer uuid : uids) {
			if (!StringUtils.isEmpty(uuid) && !uuid.equals(0)) {
				setFathersLevel(uuid);
			}
		}
	}

	private Integer getFatherId(Integer uid) {
		// TODO Auto-generated method stub
		return equityUserRepository.getOne(uid).getRecommendId();

	}

	/**
	 * 更改父亲和我的佣金比例 uid money
	 */
	public void setFathersLevel(Integer uid) {
		Integer fatherId = getFatherId(uid);
		List<EquityUser> users = new ArrayList<>();// 更改成功的代数

		// 我的钱
		EquityUser equityUser2 = equityUserRepository.getOne(uid);
		if (!StringUtils.isEmpty(equityUser2)) {
			BigDecimal myMoney = equityUser2.getMoney();
			// 父亲的钱
			EquityUser equityUser = equityUserRepository.getOne(fatherId);
			if (!StringUtils.isEmpty(equityUser)) {
				BigDecimal fatherMoney = equityUser.getMoney();
				if (!StringUtils.isEmpty(fatherMoney)) {
					// 我和父亲算一个团队
					BigDecimal teamMoney = fatherMoney.add(myMoney);
					// 保存我们团队中每个成员的佣金比例//父亲的钱加上自己的钱,然后更改我们团队的佣金比例
					BigDecimal teamLevil = getLevel(teamMoney);// 团队佣金比例
					if (equityUser.getLevel().compareTo(teamLevil) == -1) {// 小于
						System.out.println("更改equityUser用户的佣金比例");
						saveLev(equityUser, teamLevil);
						users.add(equityUser);
					}
					if (equityUser2.getLevel().compareTo(teamLevil) == -1) {
						System.out.println("更改equityUser2用户的佣金比例");
						saveLev(equityUser2, teamLevil);
						users.add(equityUser2);
					}
				}

			}
		}

	}

	/**
	 * 更改改用户的佣金比例 equityUser level
	 */
	private void saveLev(EquityUser equityUser, BigDecimal level) {
		// TODO Auto-generated method stub
		equityUser.setLevel(level);
		equityUserRepository.save(equityUser);
	}

	/**
	 * 更改我和父亲在的这个团队的佣金比例 uid
	 */
	private Integer isHaveFather(Integer uid) {
		// TODO Auto-generated method stub
		Integer fatherId;
		EquityUser equityUser = equityUserRepository.findByUId(uid);
		fatherId = equityUser.getRecommendId();
		if (!StringUtils.isEmpty(fatherId) && !fatherId.equals(0)) {
			return fatherId;
		}
		return 0;
	}

	/**
	 * 尝试更改所属团队的佣金比例 uid money
	 */
	private void changeFatherDirectpush(Integer uid, BigDecimal money) {
		// TODO Auto-generated method stub
		// 计算团队的总入股

	}

	// 尝试保存四个爷爷层的越推佣金
	/**
	 * parentList 是爷爷们的集合 爷爷的父亲们都是算祖先辈分 recommendId 就已经是爷爷的id了
	 * 
	 */
	private void setEqutites(List<EquityUser> parentList, Integer recommendId) {
		EquityUser equityUser = equityUserRepository.findByUId(recommendId);// 找到爷爷
		// if(equityUsers.size()>0) {
		// EquityUser equityUser=equityUsers.get(0);
		Integer recommendId2 = equityUser.getRecommendId();// 爷爷的推荐人==>太爷爷的id
		// if (!StringUtils.isEmpty(recommendId2) && !recommendId2.equals(0)) {
		parentList.add(equityUser);// 把爷爷装进爷爷集合里
		if (parentList.size() < ConstantUtil.GROUNDFATHER) {// 往上查询,查到四层爷爷//如果爷爷集合里少于4个爷爷就一直循环
			this.setEqutites(parentList, recommendId2);
		}

		// }
		// }

	}

	// 保存父亲 该得到的佣金
	public EquityUser saveCommission(Integer recommendId, BigDecimal commission) {

		// 保存父亲 该得到的佣金
		// List<EquityUser> equity2 =
		// equityUserRepository.findByRecommendId(recommendId);
		// if(equity2.size()>0) {
		// Integer uid=equity2.get(0).getRecommendId();

		EquityUser euser = equityUserRepository.findByUId(recommendId);// 找到父亲
		if (!StringUtils.isEmpty(euser)) {
			if (!StringUtils.isEmpty(euser.getCommission())) {
				euser.setCommission(euser.getCommission().add(commission));
			} else {
				euser.setCommission(commission);
			}

			return equityUserRepository.save(euser);
		}

		// }
		return null;

		// if(!StringUtils.isEmpty(equity2.getCommission())) {
		// equity2.setCommission(equity2.getCommission().add(commission) );
		// }
		// equity2.setCommission(commission );
		// return equityRepository.save(equity2);

	}

	// 过去总金额
	public BigDecimal getCountMoney() {
		return equityRepository.getCountMoney();

	}

	public EquityUser saveMore(Integer recommendId, BigDecimal morePush) {
		// 祖先爷爷层
		// List<EquityUser> equityUsers =
		// equityUserRepository.findByRecommendId(recommendId);
		// // 保存爷爷的越推金额
		// if(equityUsers.size()>0) {
		// Integer uid=equityUsers.get(0).getRecommendId();
		EquityUser euser = equityUserRepository.findByUId(recommendId);// 直接用传过来的爷爷id进行查找并更新越推金额的状态
		if (!StringUtils.isEmpty(euser)) {
			if (!StringUtils.isEmpty(euser.getMorePush())) {
				euser.setMorePush(euser.getMorePush().add(morePush));
			} else {
				euser.setMorePush(morePush);
			}

			return equityUserRepository.save(euser);
		}
		return null;
		// }
		// return null;
	}

	// 判断直推佣金百分比分红
	public BigDecimal getDirectPush(BigDecimal money) {
		BigDecimal directPush = new BigDecimal("0.0");
		if (money.compareTo(new BigDecimal("150000")) > -1) {
			directPush = new BigDecimal("0.25");
		} else if (money.compareTo(new BigDecimal("100000")) > -1 && money.compareTo(new BigDecimal("150000")) == -1) {
			directPush = new BigDecimal("0.20");
		} else if (money.compareTo(new BigDecimal("50000")) > -1 && money.compareTo(new BigDecimal("100000")) == -1) {
			directPush = new BigDecimal("0.15");
		} else if (money.compareTo(new BigDecimal("600")) > -1 && money.compareTo(new BigDecimal("50000")) == -1) {
			System.out.println("我是600到50000的");
			directPush = new BigDecimal("0.10");
		} else {// 如果小于600股就没有收益
			directPush = new BigDecimal("0.00");
		}
		return directPush;
	}

	// 判断直推佣金百分比分红
	public BigDecimal getLevel(BigDecimal money) {
		BigDecimal level = new BigDecimal("0");
		if (money.compareTo(new BigDecimal("150000")) > -1) {
			level = new BigDecimal("4");
		} else if (money.compareTo(new BigDecimal("100000")) > -1 && money.compareTo(new BigDecimal("150000")) == -1) {
			level = new BigDecimal("3");
		} else if (money.compareTo(new BigDecimal("50000")) > -1 && money.compareTo(new BigDecimal("100000")) == -1) {
			level = new BigDecimal("2");
		} else if (money.compareTo(new BigDecimal("600")) > -1 && money.compareTo(new BigDecimal("50000")) == -1) {
			System.out.println("我是600到50000的");
			level = new BigDecimal("1");
		} else {// 如果小于600股就没有收益
			level = new BigDecimal("0");
		}
		return level;
	}

	@Override
	public Integer deleteEquity(Integer eId) {
		// TODO Auto-generated method stub
		Equity equity = equityRepository.findOne(eId);
		equity.setIsDel(1);
		Equity equity2 = equityRepository.save(equity);
		if (StringUtils.isEmpty(equity2)) {
			return 1;
		}
		return 0;
	}

	@Override
	public Equity saveEquity(Equity equity) {
		// TODO Auto-generated method stub
		return equityRepository.save(equity);
	}

	@Override
	public Page<Equity> queryEquity(Integer page, Integer size, EquitySearch search) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Sort.Direction.ASC, "createTime");

		System.out.println(sort);
		Pageable pageable = new PageRequest(page, size, sort);
		System.out.println(pageable);
		return equityRepository.findAll(this.getWhereClause(search), pageable);
	}

	private Specification<Equity> getWhereClause(final EquitySearch search) {
		return new Specification<Equity>() {
			@Override
			public Predicate toPredicate(Root<Equity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();// 动态SQL表达式
				List<Expression<Boolean>> exList = predicate.getExpressions();// 动态SQL表达式集合

				// 判断时间
				if (search.getBegin() != null) {
					exList.add(cb.greaterThanOrEqualTo(root.<Date>get("createTime"), search.getBegin()));// 大于等于起始日期
				}
				if (search.getEnd() != null) {
					exList.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), search.getEnd()));// 小于等于截止日期
				}

				if (search.getName() != null && !" ".equals(search.getName().trim())) {
					exList.add(cb.like(root.<String>get("name"), "%" + search.getName() + "%"));
				}

				if (search.getIsDel() != null) {
					exList.add(cb.equal(root.<Integer>get("isDel"), search.getIsDel()));
				}
				return predicate;
			}
		};
	}

}
