import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


class Solution{

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Condition t1Lock = lock.newCondition();

    private static final Condition t2Lock = lock.newCondition();
    
    private static volatile boolean flag = true;

    public static void main(String[]args){

        Thread t1 = new Thread(() ->{
            lock.lock();
            try{
                int num = 1;
                while(num <= 26){
                    while(flag){
                        t2Lock.await();
                    }
                    System.out.println(Thread.currentThread().getName() + " : " + num);
                    num++;
                    flag = false;
                    t2Lock.signal();
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            } finally{
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() ->{
            lock.lock();
            try{
                int num = 0;
                while(num < 26){
                    while(!flag) {
                        t1Lock.await();
                    }
                    System.out.println(Thread.currentThread().getName() + " : " + (char)(num + 'a'));
                    num++;
                    flag = true;
                    t1Lock.signal();
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            } finally{
                lock.unlock();
            }
        });



//        Thread t2 = new Thread(() ->{
//            lock.lock();
//            int num = 0;
//            while(num < 26 ){
//                if(!flag){
//                    try{
//                        t2Lock.await();
//                        System.out.println(Thread.currentThread().getName() + " : " + (char)(num + 'a'));
//                        flag = true;
//                        num++;
//                        t1Lock.signal();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    } finally {
//
//                        lock.unlock();
//                    }
//                }
//            }
//        });

        try{
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }catch (Exception e){
            System.out.println(e);
        }



    }





}



