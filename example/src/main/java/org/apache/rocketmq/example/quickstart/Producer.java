/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.quickstart;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * This class demonstrates how to send messages to brokers using provided {@link DefaultMQProducer}.
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {

        /*
         * 创建生产者producer
         *
         * 发送消息，需要告诉你这个消息属于哪个topic，这个topic 中文解释成主题，
         * 你可以理解成同一个业务类型的消息，怎样算是同一个业务类型的消息呢？
         *
         * 比如说要做注册功能，需要发送验证码短信，发送验证码短信这个业务topic就可以是sendMessageCode；
         * 有需要发送验证码的请求，就可以生成一个发送验证码的msg，投递到mq中，然后消息消费者拉到这个消息就会处理对应的业务；
         * 当然你可以把发送短信验证码分的更细些，有发送注册验证码，有发送登录验证码，这个要看你特殊需求没。
         *
         * 这个topic可以是用RocktMQ的可视化管理平台创建，修改，也可以在消息生产者使用api创建，
         * 也可以不用管，发送消息的时候直接指定就行，默认就会创建了。
         */
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");

        /**
         * 设置namesrv地址
         */
        producer.setNamesrvAddr("127.0.0.1:9876");

        /*
         * 启动producer
         */
        producer.start();

        for (int i = 0; i < 1000; i++) {
            try {

                /*
                 * 创建消息
                 */
                Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );

                /*
                 * 发送消息
                 */
                SendResult sendResult = producer.send(msg);

                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        /*
         * Shut down once the producer instance is not longer in use.
         */
        producer.shutdown();
    }
}
