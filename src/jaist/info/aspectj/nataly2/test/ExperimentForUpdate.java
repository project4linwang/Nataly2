package jaist.info.aspectj.nataly2.test;



import jaist.info.aspectj.nataly2.TMatcher.RefinedTMatcher;
import jaist.info.aspectj.nataly2.TMatcher.TMatcher;
import jaist.info.aspectj.nataly2.metamodel.Element;
import jaist.info.aspectj.nataly2.metamodel.PCSignature;
import jaist.info.aspectj.nataly2.metamodel.RelationElements;
import jaist.info.aspectj.nataly2.metamodel.RelationshipGraph;
import jaist.info.aspectj.nataly2.metamodel.SElement;
import jaist.info.aspectj.nataly2.metamodel.TPattern;
import jaist.info.aspectj.nataly2.metamodel.TPatternPath;
import jaist.info.aspectj.nataly2.relationanalyzer.RelationshipGraphGenerator;
import jaist.info.aspectj.nataly2.seed.Signature;
import jaist.info.aspectj.nataly2.tools.NameWildCardPatternUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import edu.stanford.nlp.util.Pair;

/**
 * The test class for the updated version of program. 
 * @author suse-wl
 *
 */
public class ExperimentForUpdate {
	private RelationElements relation_elems=RelationElements.getRelationElements();
	private  List<Element> elems=relation_elems.getElementList();
    private Logger log=Logger.getLogger(ExperimentForUpdate.class);
    
	public List<String> getRelevants(String path,String pcname){
		String file=path+pcname;
		StringBuilder fileNameBuilder = new StringBuilder(file);
		fileNameBuilder.append("#" + "npcjps.dat");
		String filename=fileNameBuilder.toString();
		fileNameBuilder.toString();  
		List<String> relevants=new LinkedList<String>();
		try {
			FileReader reader=new FileReader(filename);
			BufferedReader br=new BufferedReader(reader);
			String s=null;
			
			while((s=br.readLine())!=null){
				if(s!=""){
					relevants.add(s);
				}
				
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relevants;
		
	}
	
	private void CalulateNWROC(List<String> nwlist,List<String> relevants,List<String> elementlist){
		double F1=0.00d;
		double TPR=0.0d;
		double FPR=0.0d;
		int TP=0;
		int FP=0;
		int TN=0;
		int FN=0;
		if(elementlist!=null){
			if(elementlist.size()>0){
				for(String elem:elementlist){
					if(nwlist.contains(elem)){
						if(relevants.contains(elem)){
							TP++;
						}
						else{
							FP++;
						}
					}
					else{
						if(relevants.contains(elem)){
							FN++;
						}
						else{
							TN++;
						}
					}						
				}
				if((TP+FN)>0){
					TPR=(double)TP/(TP+FN);
				}
			  	
			  	if((FP+TN)>0){
			  		FPR=(double)FP/(FP+TN);
			  	}
			  	if((2*TP+FP+FN)>0){
			  		F1=(double)2*TP/(2*TP+FP+FN);
			  	}
			}
		}
		   
		log.info("TPR: "+TPR+" FPR: "+FPR+" F1: "+F1);
	}
	private void CalulateROC(double threshold,Map<String,Double> ranklist,List<String> relevants){
		double F1=0.00d;
		double TPR=0.0d;
		double FPR=0.0d;
		int TP=0;
		int FP=0;
		int TN=0;
		int FN=0;
		if(ranklist!=null){
			if(ranklist.size()>0){
				for(Entry<String,Double> entry:ranklist.entrySet()){
					double rate=entry.getValue();
					if(rate>=threshold){
						if(relevants.contains(entry.getKey())){
							TP++;
						}
						else{
							FP++;
						}
					}
					else{
						if(relevants.contains(entry.getKey())){
							FN++;
						}
						else{
							TN++;
						}
					}
				}
				if((TP+FN)>0){
					TPR=(double)TP/(TP+FN);
				}
			  	
			  	if((FP+TN)>0){
			  		FPR=(double)FP/(FP+TN);
			  	}
			  	if((2*TP+FP+FN)>0){
			  		F1=(double)2*TP/(2*TP+FP+FN);
			  	}
			}
		}
		   
		log.info("Threshold is :"+threshold+" TPR: "+TPR+" FPR: "+FPR+" F1: "+F1);
	}
	/**
	 * The test method for the program which have been updated.
	 * @param pc_full is the contents of pointcut
	 * @param relevants is the set of relevant data.
	 * @param npcjps_list is the set of joinpoint shadows which are captured by name-based pointcut.
	 * @param trpattern is the TPattern
	 * @param elementlist is the set of join point shadows.
	 * @param pcname is the name of pointcut.
	 */
	public void test(String pc_full,List<String> relevants,List<String> npcjps_list,TPattern trpattern,List<String> elementlist,String pcname){

		log.info("Point Cut: ");
		log.info(pc_full);
		for(String jps:relevants){
			log.info("Relevant JoinPoint Shadows: "+jps);
		}
		//Compare Name-based Point cut:
		int npc_relevantNum=0;
		int npc_retrievedNum=0;
		List<Pair<Double,Double>> npc_pair=new LinkedList<Pair<Double,Double>>();
		for(String npcjps:npcjps_list){
			npc_retrievedNum++;
			Pair<Double,Double> p=new Pair<Double,Double>();
			if(relevants.contains(npcjps)){
				npc_relevantNum++;
			}
			p.setFirst((double)npc_relevantNum/npc_retrievedNum);
			p.setSecond((double)npc_relevantNum/relevants.size());
			npc_pair.add(p);
		}
		log.info("********************************************************");
		if(npcjps_list.size()>0){
			log.info("Name based PointCut Join Point Shadows:");
			for(String nr_jps:npcjps_list){					
				log.info(nr_jps);
			}
			
		}
		double n_precision=(double)npc_relevantNum/npc_retrievedNum;
		Double ndp=new Double(n_precision);
		if(!ndp.isNaN()){
			BigDecimal bp=new BigDecimal(n_precision);
			n_precision=bp.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		double n_recall=(double)npc_relevantNum/relevants.size();
		Double ndr=new Double(n_recall);
		if(!ndr.isNaN()){
			BigDecimal br=new BigDecimal(n_recall);
			n_recall=br.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		log.info("Name based PointCut's precision="+n_precision*100.00f+"%");
		log.info("Name based PointCut's recall="+n_recall*100.00f+"%");
		log.info("Data for precisioan-recall curve");
		
		if(npc_pair.size()>0){
			double sum_p=0.000d;
			
			for(Pair<Double,Double> p:npc_pair){
				sum_p+=p.first;
			
				log.info("Precision: "+p.first*100.00f+"%");
				log.info("Recall: "+p.second*100.00f+"%");
				log.info("-------------------------------------------------");
			}
			log.info("Name based PointCut Average Precision: "+sum_p/npc_pair.size()*100.00d+"%");
			
		}
		log.info("********************************************************");

		//Compare Analysis based pointcut:
		if(trpattern!=null){
			testAnalysisBasedPointcuts(relevants,elementlist,trpattern,pcname);
		}
		
	}
	private void testAnalysisBasedPointcuts(List<String> relevants,List<String> elementlist,TPattern pattern,String pcname){
		
		if(elementlist!=null){			
			List<RelationshipGraph> graphList=generateRG(elementlist);
			List<RelationshipGraph> tmpgraph=new LinkedList<RelationshipGraph>();						
			List<TPatternPath> kp_trpattern=pattern.getKHeadTPattern(); 
			Map<String,Double> rank_list=new HashMap<String,Double>();
			for(RelationshipGraph rt:graphList){
				if(!isContain(tmpgraph,rt)){
					tmpgraph.add(rt);
				}
			}
			//
			TMatcher matcher=new TMatcher();
			RefinedTMatcher rmatcher=new RefinedTMatcher();
			//1.Name-Wild-pattern:
			PCSignature psc=NameWildCardPatternUtil.readNWPattern(relation_elems.getFilePath(), pcname);
			List<String> nw_jps=CatchRelativeElement(psc.getSignatures(),elementlist);
			
			List<Pair<Double,Double>> nw_jps_curve=new LinkedList<Pair<Double,Double>>();
			int retrievedNum=0;
			int relevantNum=0;
			if(nw_jps!=null){
				if(nw_jps.size()>0){
					for(String jps:nw_jps){
						retrievedNum++;
						Pair<Double,Double> p=new Pair<Double,Double>();
						if(relevants.contains(jps)){
							relevantNum++;
							
						}
						p.setFirst((double)relevantNum/retrievedNum);
						p.setSecond((double)relevantNum/relevants.size());
						nw_jps_curve.add(p);
					}
					
					
				}
			}	
			log.info("********************************************************");
	
			if(nw_jps.size()>0){
			
				log.info("Name-Wild Pattern Retrieved Join Point Shadows:");
				for(String s_jps:nw_jps){
					log.info(s_jps);
				}
			}
			double nw_precision=(double)relevantNum/retrievedNum;
			Double nwdp=new Double(nw_precision);
			if(!nwdp.isNaN()){
				BigDecimal bp=new BigDecimal(nw_precision);
				nw_precision=bp.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			double nw_recall=(double)relevantNum/relevants.size();
			Double nwdr=new Double(nw_recall);
			if(!nwdr.isNaN()){
				BigDecimal br=new BigDecimal(nw_recall);
				nw_recall=br.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			}	
			log.info("Name-Wild Pattern 's precision="+nw_precision*100.00f+"%");
			log.info("Name-Wild Pattern 's recall="+nw_recall*100.00f+"%");
			CalulateNWROC(nw_jps,relevants,elementlist);
			log.info("Data for precisioan-recall curve");
			
			if(nw_jps_curve.size()>0){
				double sum_p=0.000d;
				for(Pair<Double,Double> p:nw_jps_curve){
					sum_p+=p.first;
					log.info("Precision: "+p.first*100.00f+"%");
					log.info("Recall: "+p.second*100.00f+"%");
					log.info("-------------------------------------------------");
				}
				log.info("Name-Wild Pattern  Average Precision: "+sum_p/nw_jps_curve.size()*100.00d+"%");
			}
			log.info("********************************************************");
			
			//2.key-head tpattern:
			 if(kp_trpattern!=null){
				 if(kp_trpattern.size()>0){
					 for(TPatternPath p:kp_trpattern){
						 log.info("KeyHead: "+p.getData());
					 }
				 }
			 }
			
			if(kp_trpattern!=null){
				if(kp_trpattern.size()>0){
					int kp_relevantNum=0;
					int kp_retrievedNum=0;
					List<String> kp_jps=new LinkedList<String>();
					List<Pair<Double,Double>> kp_p_and_c=new LinkedList<Pair<Double,Double>>();
					Map<String,Double> kp_rank_list=new HashMap<String,Double>();
					for(RelationshipGraph rt: tmpgraph){
						
							if(matcher.isTMatched(rt, kp_trpattern)){
								kp_retrievedNum++;
								String c_name=rt.getRoot().getData().getTraditionalFullName();
								kp_jps.add(c_name);
								Pair<Double,Double> p=new Pair<Double,Double>();
								if(relevants.contains(c_name)){
									kp_relevantNum++;
								}
								p.setFirst((double)kp_relevantNum/kp_retrievedNum);
								p.setSecond((double)kp_relevantNum/relevants.size());
								kp_p_and_c.add(p);
							}
							double match_rate= matcher.tmatches(rt, kp_trpattern);
							kp_rank_list.put(rt.getRoot().getData().getTraditionalFullName(), match_rate);																			
					}
					log.info("********************************************************");
					if(kp_jps.size()>0){
						log.info("KeyHead Retrieved Join Point Shadows:");
						for(String s_jps:kp_jps){
							log.info(s_jps);
						}
					}
					double kp_precision=(double)kp_relevantNum/kp_retrievedNum;
					Double kpd=new Double(kp_precision);
					if(!kpd.isNaN()){
						BigDecimal bp=new BigDecimal(kp_precision);
						kp_precision=bp.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					double kp_recall=(double)kp_relevantNum/relevants.size();
					Double kpdr=new Double(kp_recall);
					if(kpdr.isNaN()){
						BigDecimal br=new BigDecimal(kp_recall);
						kp_recall=br.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
					}

					log.info("KeyHead TPattern's precision="+kp_precision*100.00f+"%");
					log.info("KeyHead TPattern's recall="+kp_recall*100.00f+"%");
					log.info("Data for precisioan-recall curve");
					
					if(kp_p_and_c.size()>0){
						double sum_p=0.000d;
						for(Pair<Double,Double> pair:kp_p_and_c){
							sum_p+=pair.first;					
							log.info("Precision: "+pair.first*100.00f+"%");
							log.info("Recall: "+pair.second*100.00f+"%");
							log.info("-------------------------------------------------");
						}					
						log.info("KepPoint Average Precision: "+sum_p/kp_p_and_c.size()*100.00d+"%");
					}
					log.info("KeyPoint Pattern ROC:");
					for(float j=10;j<=100;j+=5){
						double threshold=round(2,(double)j/100.0d);
						CalulateROC(threshold,kp_rank_list,relevants);
					}
					log.info("KepPoint TPattern Ranking Data:");
					if(kp_rank_list.size()>0){
						CaculateRankingFun(kp_rank_list,relevants,0.5d);
						CaculateRankingFun(kp_rank_list,relevants,0.8d);
					}
					log.info("********************************************************");
					//Refined KeyHeadT-Pattern algorithm
					List<RelationshipGraph> canditree=rmatcher.matchTRefinedCandidate(tmpgraph, kp_trpattern,nw_jps);
					List<String> shortest_retrieved_jps=new LinkedList<String>();
					List<Pair<Double,Double>> shorest_p_and_c=new LinkedList<Pair<Double,Double>>();
					
					int snt_matchnum=0;
					int stp_number=0;
					if(canditree!=null){
						if(canditree.size()>0){
							for(RelationshipGraph rt:canditree){
								stp_number++;
								String c_name=rt.getRoot().getData().getTraditionalFullName();
						    	shortest_retrieved_jps.add(c_name);
						    	Pair<Double,Double> p=new Pair<Double,Double>();
						    	
								if(relevants.contains(c_name)){
									snt_matchnum++;
								}
								p.setFirst((double)snt_matchnum/stp_number);
								p.setSecond((double)snt_matchnum/relevants.size());
								shorest_p_and_c.add(p);
							}
						}
					}
					log.info("********************************************************");
					if(shortest_retrieved_jps.size()>0){
						log.info("Refined-KP Retrieved Join Point Shadows:");
						for(String s_jps:shortest_retrieved_jps){
							log.info(s_jps);
						}
					}
					double sh_precision=(double)snt_matchnum/stp_number;
					Double shdp=new Double(sh_precision);
					if(!shdp.isNaN()){
						BigDecimal bp=new BigDecimal(sh_precision);
						sh_precision=bp.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
					}
					double sh_recall=(double)snt_matchnum/relevants.size();
					Double shdr=new Double(sh_recall);
					if(!shdr.isNaN()){
						BigDecimal br=new BigDecimal(sh_recall);
						sh_recall=br.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
					}

					log.info("Refined-KH TPattern's precision="+sh_precision*100.00f+"%");
					log.info("Refined-KH TPattern's recall="+sh_recall*100.00f+"%");
					log.info("Data for precisioan-recall curve");
					
					if(shorest_p_and_c.size()>0){
						double sum_p=0.000d;
						for(Pair<Double,Double> p:shorest_p_and_c){
							sum_p+=p.first;							
							log.info("Precision: "+p.first*100.00f+"%");
							log.info("Recall: "+p.second*100.00f+"%");
							log.info("-------------------------------------------------");
						}
						log.info("Refined-KP Average Precision: "+sum_p/shorest_p_and_c.size()*100.00d+"%");					
					}					
				}
			}
		}
	}
	private double round(int size,double value){
		Double shdp=new Double(value);
		if(!shdp.isNaN()){
			BigDecimal bp=new BigDecimal(value);
			return bp.setScale(size,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		else{
			return value;
		}
	}
	private List<String> CatchRelativeElement(List<Signature> signatures,List<String> elems){

		List<String> matchedList=new LinkedList<String>();
		List<SElement> selems=new LinkedList<SElement>();
		if(elems!=null){
			if(elems.size()>0){
				for(String el:elems){
					SElement se=FindSElement(el);
					if(se!=null){
						selems.add(se);
					}
					
				}
			}
		}
		if(signatures!=null){
			for(Signature sig: signatures){
				matchedList.addAll( sig.CatchRelativeElements(selems));
			}
		}
		return matchedList;
	}
	private SElement FindSElement(String fullname){		
		List<SElement> selems=relation_elems.getSelementlist();
		if(selems!=null){
			for(SElement el:selems){
				if(el.getTraditionalFullName().equals(fullname)){
					return el;
				}
			}
		}
	 return null;
   }
	
	private void CaculateRankingFun(Map<String,Double> ranklist,List<String> relevants,double threshold){
		if(ranklist!=null){
			if(ranklist.size()>0){
				//System.out.println("Threshold is :"+threshold);				
                log.info("Threshold is :"+threshold);
				List<Entry<String,Double>> sortedmap=sortMap(ranklist);
				double sum_precision=0.000d;
				int precision_count=0;
				int rank=0;
				int retrieved_num=0;
				int relevant_num=0;
				for(Entry<String,Double> entry:sortedmap){
					String isrelevant="-";
					double rate=entry.getValue();
									
					
					if(rate>=threshold){
						retrieved_num++;
						if(relevants.contains(entry.getKey())){
							relevant_num++;
							isrelevant="+";
						}													
					}
					if(retrieved_num>0){
						rank++;
						double precision=(double)relevant_num/retrieved_num;
						BigDecimal bp=new BigDecimal(precision);
						precision=bp.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
						if(isrelevant.equals("+")){
							sum_precision=sum_precision+precision;
							precision_count++;
						}
						double recall=(double)relevant_num/relevants.size();
						BigDecimal br=new BigDecimal(recall);
						recall=br.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
						if(!(rate==0.0d && recall==1.0d)){
							log.info("Rank: "+rank+"  Rate: "+rate*100.00d+"%"+"  "+isrelevant+"  P: "+precision*100.00d+"%"+"  R: "+recall*100.00d+"%"+"  JPS: "+entry.getKey());
							
						}
						
					}
				}
				if(precision_count>0){
					log.info("Ranking Average Precision: "+(double)sum_precision/precision_count*100.00d+"%");
				}
				

			}
		}
		
	}
	private List<Entry<String,Double>> sortMap(Map<String,Double> map){
		List<Entry<String,Double>> arrayList=new ArrayList<Entry<String,Double>>(map.entrySet());
		Collections.sort(arrayList,new Comparator<Entry<String,Double>>(){
		   public int compare(Entry<String,Double> e1, Entry<String,Double> e2){
			  return (e2.getValue()).compareTo(e1.getValue());
		   }
		});
		return arrayList;
	}
	private List<RelationshipGraph> generateRG(List<String> jplist){
		RelationshipGraphGenerator rtGenerator=new RelationshipGraphGenerator();
		List<Element> joinpointList=new LinkedList<Element>();
		if(jplist!=null){
			if(jplist.size()>0){
				for(String jp:jplist){
					joinpointList.add(FindElement(jp,elems));
				}
			}
		}
		 List<RelationshipGraph> treeList=rtGenerator.generateRG(joinpointList);
		 
		 
		 return treeList;
	}
	private Element FindElement(String fullname,List<Element> elementlist){
		 for(Element el: elementlist){
			 if(el.getTraditionalFullName().equals(fullname)){
				 return el;
			 }
		 }
		 return null;
	 }
	private boolean isContain(List<RelationshipGraph> rts,RelationshipGraph tree){
		for(RelationshipGraph rt:rts){
			if(rt.getRoot().getData().getTraditionalFullName().equals(tree.getRoot().getData().getTraditionalFullName())){
				return true;
			}
		}
		return false;
	}
}
