package swimmer

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariDataSource

import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

import scala.concurrent.duration.FiniteDuration

import scalikejdbc.*

object Store:
  def apply(config: Config) = new Store( dataSource(config) )

  def dataSource(config: Config): DataSource =
    val ds = HikariDataSource()
    ds.setDataSourceClassName(config.getString("db.driver"))
    ds.addDataSourceProperty("url", config.getString("db.url"))
    ds.addDataSourceProperty("user", config.getString("db.user"))
    ds.addDataSourceProperty("password", config.getString("db.password"))
    ds

final class Store(dataSource: DataSource):
  ConnectionPool.singleton( DataSourceConnectionPool(dataSource) )

  def listSwimmers(accountId: Long): List[Swimmer] =
    DB readOnly { implicit session =>
      sql"select * from swimmer where account_id = $accountId order by name"
        .map(rs =>
          Swimmer(
            rs.long("id"),
            rs.long("account_id"),
            rs.string("name"), 
          )
        )
        .list()
    }

  def addSwimmer(swimmer: Swimmer): Long =
    DB localTx { implicit session =>
      sql"""
        insert into swimmer(account_id, name) values(${swimmer.accountId}, ${swimmer.name})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateSwimmer(swimmer: Swimmer): Long =
    DB localTx { implicit session =>
      sql"""
        update swimmer set name = ${swimmer.name}
        where id = ${swimmer.id}
        """
        .update()
      swimmer.id
    }

  def listSessions(swimmerId: Long): List[Session] =
    DB readOnly { implicit session =>
      sql"select * from session where swimmer_id = $swimmerId order by datetime desc"
        .map(rs =>
          Session(
            rs.long("id"),
            rs.long("swimmer_id"),
            rs.int("weight"),
            rs.string("weight_unit"),
            rs.int("laps"),
            rs.int("lap_distance"),
            rs.string("lap_unit"),
            rs.string("style"),
            rs.boolean("kickboard"),
            rs.boolean("fins"),
            rs.int("minutes"),
            rs.int("seconds"),
            rs.int("calories"),
            rs.long("datetime")
          )
        )
        .list()
    }

  def addSession(sess: Session): Long =
    DB localTx { implicit session =>
      sql"""
        insert into session(swimmer_id, weight, weight_unit, laps, lap_distance,
        lap_unit, style, kickboard, fins, minutes, seconds, calories, datetime)
        values(${sess.swimmerId}, ${sess.weight}, ${sess.weightUnit}, ${sess.laps},
        ${sess.lapDistance}, ${sess.lapUnit}, ${sess.style}, ${sess.kickboard},
        ${sess.fins}, ${sess.minutes}, ${sess.seconds}, ${sess.calories}, ${sess.datetime})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateSession(sess: Session): Long =
    DB localTx { implicit session =>
      sql"""
        update session set weight = ${sess.weight}, weight_unit = ${sess.weightUnit},
        laps = ${sess.laps}, lap_distance = ${sess.lapDistance}, lap_unit = ${sess.lapUnit},
        style = ${sess.style}, kickboard = ${sess.kickboard}, fins = ${sess.fins},
        minutes = ${sess.minutes}, seconds = ${sess.seconds}, calories = ${sess.calories},
        datetime = ${sess.datetime}
        where id = ${sess.id}
        """
        .update()
      sess.id
    }