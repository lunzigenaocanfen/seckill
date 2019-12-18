CREATE DEFINER=`root`@`localhost` PROCEDURE `execute_seckill`(IN  v_seckill_id BIGINT,
 IN  v_phone BIGINT,
 IN  v_kill_time TIMESTAMP,
 OUT r_result INT
)
BEGIN
  #Routine body goes here...
	DECLARE insert_count int DEFAULT 0;
	START TRANSACTION;
	insert ignore into success_killed(seckill_id, user_phone, state)
        values (v_seckill_id, v_phone, 0);
	SELECT ROW_COUNT() INTO insert_count;
	IF (insert_count = 0) THEN
	  ROLLBACK;
	  SET r_result = -1;
  ELSEIF (insert_count < 0) THEN
		ROLLBACK;
		SET r_result = -2;
	ELSE
		update
          seckill
        set
          number = number - 1
        where seckill_id = v_seckill_id
        and start_time < v_kill_time
        and end_time > v_kill_time
        and number > 0;
		SELECT ROW_COUNT() INTO insert_count;
		IF (insert_count = 0) THEN
			ROLLBACK;
			SET r_result = 0;
		ELSEIF (insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -2;
		ELSE
			COMMIT;
			SET r_result = 1;
		END IF;
	END IF;


END